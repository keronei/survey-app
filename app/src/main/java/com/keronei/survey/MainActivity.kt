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

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keronei.survey.core.Constants.REQUEST_IMAGE_CAPTURE
import com.keronei.survey.presentation.ui.viewmodel.MainViewModel
import com.keronei.survey.presentation.ui.viewmodel.QuestionsHelperViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 *  Main Activity which is the Launcher Activity
 */

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val helperViewModel: QuestionsHelperViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            R.menu.main_menu -> {
                val alertDialog = MaterialAlertDialogBuilder(this).setTitle("+254739224261")
                    .setMessage("You are currently logged in.")
                    .setIcon(R.drawable.ic_baseline_account_circle_24)
                    .create()

                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Logout") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    Toast.makeText(this, "Remove user account.", Toast.LENGTH_SHORT).show()
                }

                alertDialog.show()
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

}