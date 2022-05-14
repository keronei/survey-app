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
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.keronei.survey.R
import com.keronei.survey.core.Constants.NOTIFICATION_CHANNEL_ID
import com.keronei.survey.domain.repositories.SubmissionsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

@HiltWorker
class SubmissionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val submissionsRepository: SubmissionsRepository,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {

        return try {
            val availableSubmissions = submissionsRepository.submitCurrentResponses().first()
            displayNotification(
                applicationContext,
                availableSubmissions.submissionMessage,
                availableSubmissions.status
            )

            /** Step 1: Get the uris
             // prepareToSendImages()

             Step 2: Send the images

             Step 3: Clean Up the disk.
             **/

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

    private suspend fun structAndSendData(bitmap: Bitmap) {
        val bytes = getBytesFromBitmap(
            bitmap
        )
        val photo = toMultiPartFile(bytes, "profile${System.currentTimeMillis()}")

        // Now pass them to api, we are using Retrofit
        submissionsRepository.sendMediaFiles(photo)
    }

    private fun toMultiPartFile(
        byteArray: ByteArray,
        fileName: String
    ): MultipartBody.Part {
        val reqFile: RequestBody =
            byteArray.toRequestBody("image/*".toMediaTypeOrNull(), 0, byteArray.size)

        return MultipartBody.Part.createFormData(
            "photo", // photo is the name in the api
            fileName,
            reqFile
        )
    }

    private fun getBytesFromBitmap(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }

    // send the images
    private suspend fun prepareToSendImages(uri: Uri) {
        try {
            val bitmap = getBitmapRequest(uri).submit().get()
            structAndSendData(bitmap)
        } catch (_: Exception) {
            this.getBitmapRequest(uri).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    CoroutineScope(Dispatchers.Default).launch {
                        structAndSendData(resource)
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        }
    }

    // smoothen the image with glide
    private fun getBitmapRequest(
        uri: Uri?
    ): RequestBuilder<Bitmap> {
        return Glide.with(applicationContext)
            .asBitmap()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .override(1080, 1080)
            .load(uri)
    }
}