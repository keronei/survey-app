/*
 * Copyright 2021 Keronei Lincoln
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keronei.survey

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationBarItemView
import com.google.common.hash.Hashing
import com.keronei.survey.core.Constants
import com.keronei.survey.core.Constants.IS_LOGGED_IN_KEY
import com.keronei.survey.core.Constants.PASS_KEY
import com.keronei.survey.core.Constants.PHONE_NUMBER_KEY
import com.keronei.survey.core.Constants.REQUEST_IMAGE_CAPTURE
import com.keronei.survey.core.Constants.WORKMAGAER_TAG
import com.keronei.survey.core.workmanager.SubmissionsWorker
import com.keronei.survey.presentation.ui.viewmodel.QuestionsHelperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 *  Main Activity which is the Launcher Activity
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val helperViewModel: QuestionsHelperViewModel by viewModels()

    private lateinit var workManager: WorkManager

    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    private lateinit var controller: NavController

    var redirected = false

    @Inject
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        syncResponsesPeriodically()

        navigateToHomeOrLogin()

        preparePassword()
    }

    private fun preparePassword() {
        val hasPasswordSet = preferences.contains(PASS_KEY)
        if (!hasPasswordSet) {
            // Only set the password once.
            val passwordHash =
                Hashing.sha256()
                    .hashString(getString(R.string.pass), Charset.defaultCharset())

            preferences
                .edit()
                .putLong(PASS_KEY, passwordHash.padToLong())
                .apply()
        }


    }

    private fun navigateToHomeOrLogin() {

        val hostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_main)

        if (hostFragment != null) {
            controller = hostFragment.findNavController()

            controller.addOnDestinationChangedListener { controller, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment -> {
                        val isLoggedIn = preferences.getBoolean(IS_LOGGED_IN_KEY, false)
                        if (!isLoggedIn) {
                            controller.navigate(R.id.action_homeFragment_to_loginFragment)
                        }
                    }
                }
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            R.id.profile -> {

                if (this::controller.isInitialized) {

                    val currentDestination = controller.currentDestination

                    if (currentDestination?.id != R.id.loginFragment) {

                        val savedPhone = preferences.getString(PHONE_NUMBER_KEY, "Empty Phone")

                        MaterialAlertDialogBuilder(this).setMessage("You are currently logged in.")
                            .setPositiveButton("Cancel") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setNegativeButton("Logout") { dialog, _ ->
                                dialog.dismiss()

                                if (this::controller.isInitialized) {

                                    val editor = preferences.edit()
                                    editor.remove(IS_LOGGED_IN_KEY)
                                    editor.remove(PHONE_NUMBER_KEY)
                                    editor.apply()

                                    controller.navigate(R.id.action_homeFragment_to_loginFragment)
                                }

                            }
                            .setTitle(savedPhone)
                            .setIcon(R.drawable.ic_baseline_account_circle_24)
                            .create()
                            .show()
                    } else {
                        MaterialAlertDialogBuilder(this)
                            .setMessage(
                                "You are currently not logged in. " +
                                        "\n\nUse your phone number and provided password to log in."
                            )
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setTitle("No Account")
                            .setIcon(R.drawable.ic_baseline_account_circle_24)
                            .create()
                            .show()
                    }
                }
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Send a flag to the observer
            helperViewModel.setBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888))
        } else {
            Toast.makeText(this, "Capture was not successful.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun syncResponsesPeriodically() {

        workManager = WorkManager.getInstance(applicationContext)

        // Launch work manager job if there's any internet connection.
        // Also, if there's any responses to be synced, the repo will perform a check.
        outputWorkInfo = workManager.getWorkInfosByTagLiveData(WORKMAGAER_TAG)

        val workAlreadyStarted = outputWorkInfo.value?.filter { workInfo ->
            workInfo.tags.contains(WORKMAGAER_TAG)
        }

        if (workAlreadyStarted.isNullOrEmpty()) {

            val internetRequirementConstraint =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            val notifyTemperatureWorkRequest =
                PeriodicWorkRequestBuilder<SubmissionsWorker>(15, TimeUnit.MINUTES)
                    .setConstraints(internetRequirementConstraint)
                    .addTag(WORKMAGAER_TAG).build()

            workManager.enqueueUniquePeriodicWork(
                Constants.WORK_ID,
                ExistingPeriodicWorkPolicy.REPLACE,
                notifyTemperatureWorkRequest
            )
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notificationchannel_name)

            val descriptionText = getString(R.string.channel_desc)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}