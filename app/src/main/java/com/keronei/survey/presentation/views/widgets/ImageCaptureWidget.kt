package com.keronei.survey.presentation.views.widgets

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.keronei.survey.R
import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.Constants.REQUEST_IMAGE_CAPTURE
import com.keronei.survey.core.Constants.REQUEST_STORAGE_PERMISSION
import com.keronei.survey.databinding.ImageWidgetBinding
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.views.QuestionWidget
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")
class ImageCaptureWidget(
    private val activity: Activity,
    context: Context,
    questionDetails: QuestionDefinition
) : QuestionWidget(context, questionDetails) {
    lateinit var currentPhotoPath: String

    lateinit var layout: ImageWidgetBinding

    init {
        setupLayout()
    }

    override fun getAnswer(): AnswerData? {
        return if (this::currentPhotoPath.isInitialized) {
            AnswerData(getQuestionDefinition().id, currentPhotoPath)
        } else {
            null
        }
    }

    override fun saveCurrentAnswer(): Boolean {
        val answer = getAnswer() ?: return false
        getQuestionDefinition().answerData = answer
        return true
    }

    private fun setupLayout() {
        layout = ImageWidgetBinding.bind(this)

        layout.btnCapture.setOnClickListener {
            if (this::currentPhotoPath.isInitialized) {
                layout.btnCapture.text = context.getString(R.string.retake_picture)
            } else {
                layout.btnCapture.text = context.getString(R.string.take_picture)

            }

            dispatchTakePictureIntent()
        }

        addAnswerView(layout.root)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            EasyPermissions.requestPermissions(
                activity,
                "Need to store the image.",
                REQUEST_STORAGE_PERMISSION
            )

            if (EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                takePictureIntent.resolveActivity(context.packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File

                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {

                        // display the image
                        val bitmap = BitmapFactory.decodeFile(it.absolutePath)

                        layout.imagePreview.setImageBitmap(bitmap)

                        val photoURI: Uri = FileProvider.getUriForFile(
                            context,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(
                            activity,
                            takePictureIntent,
                            REQUEST_IMAGE_CAPTURE,
                            Bundle()
                        )
                    }
                }
            }
        }
    }


}