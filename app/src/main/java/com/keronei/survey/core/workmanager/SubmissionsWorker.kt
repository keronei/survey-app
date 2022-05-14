/*
 * Copyright 2022 Keronei Lincoln
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
package com.keronei.survey.core.workmanager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.keronei.survey.R
import com.keronei.survey.core.Constants.NOTIFICATION_CHANNEL_ID
import com.keronei.survey.domain.repositories.SubmissionsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class SubmissionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val submissionsRepository: SubmissionsRepository
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        return try {
            val availableSubmissions = submissionsRepository.submitCurrentResponses().first()
            displayNotification(
                applicationContext,
                availableSubmissions.submissionMessage,
                availableSubmissions.status
            )

            if (availableSubmissions.status) Result.success() else Result.failure()
        } catch (exception: Exception) {
            exception.printStackTrace()
            displayNotification(
                applicationContext,
                "Failed to sync. Will retry in a moment.", false
            )
            Result.failure()
        }
    }

    private fun displayNotification(context: Context, message: String, status: Boolean) {
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(if (status) R.drawable.ic_baseline_cloud_sync_24 else R.drawable.ic_baseline_sync_problem_24)
            .setContentTitle(context.getString(R.string.nitification_title))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            // Display the notification
            notify(message.length, builder.build())
        }
    }
}