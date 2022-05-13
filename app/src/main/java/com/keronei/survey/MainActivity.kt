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
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.work.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keronei.survey.core.Constants
import com.keronei.survey.core.Constants.REQUEST_IMAGE_CAPTURE
import com.keronei.survey.core.Constants.WORKMAGAER_TAG
import com.keronei.survey.core.workmanager.SubmissionsWorker
import com.keronei.survey.presentation.ui.viewmodel.QuestionsHelperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.TimeUnit

/**
 *  Main Activity which is the Launcher Activity
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val helperViewModel: QuestionsHelperViewModel by viewModels()

    lateinit var workManager: WorkManager

    private lateinit var outputWorkInfo: LiveData<List<WorkInfo>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        syncResponsesPeriodically()
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

                MaterialAlertDialogBuilder(this).setMessage("You are currently logged in.")
                    .setPositiveButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("Logout") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(this, "Remove user account.", Toast.LENGTH_SHORT).show()
                    }
                    .setTitle("+254739224261")
                    .setIcon(R.drawable.ic_baseline_account_circle_24)
                    .create()
                    .show()
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
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}