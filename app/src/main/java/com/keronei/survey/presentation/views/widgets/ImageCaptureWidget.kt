package com.keronei.survey.presentation.views.widgets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import com.google.android.material.button.MaterialButton
import com.keronei.survey.R
import com.keronei.survey.core.AnswerData
import com.keronei.survey.core.Constants.REQUEST_IMAGE_CAPTURE
import com.keronei.survey.domain.models.QuestionDefinition
import com.keronei.survey.presentation.ui.viewmodel.QuestionsHelperViewModel
import com.keronei.survey.presentation.views.QuestionWidget
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")
class ImageCaptureWidget(
    private val activity: Activity,
    context: Context,
    questionDetails: QuestionDefinition,
    helperViewModel: QuestionsHelperViewModel,
    private val scope: CoroutineScope
) : QuestionWidget(context, questionDetails) {
    private lateinit var currentPhotoPath: String

    lateinit var layout: View

    lateinit var imageView: ImageView

    private lateinit var captureBtn: MaterialButton

    init {
        setupLayout()
        scope.launch {
            helperViewModel.imageWaitingState.collect { bitmap ->
                if (bitmap != null && this@ImageCaptureWidget::currentPhotoPath.isInitialized) {
                    try {
                        // set this as current answer because next button is finish and user might go back.
                        saveCurrentAnswer()
                        setPictureInPreview()
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                    val captureBtn = layout.findViewById<MaterialButton>(R.id.btn_capture)

                    captureBtn.text = context.getString(R.string.retake_picture)
                }
            }
        }
    }

    private fun setPictureInPreview() {
        scope.launch(Dispatchers.IO) {
            val presetBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            // switch thread.
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(presetBitmap)
            }
        }
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
        layout = inflate(context, R.layout.image_widget, null)

        captureBtn = layout.findViewById(R.id.btn_capture)

        imageView = layout.findViewById(R.id.image_preview)

        captureBtn.setOnClickListener {
            if (this::currentPhotoPath.isInitialized) {
                captureBtn.text = context.getString(R.string.retake_picture)
            } else {
                captureBtn.text = context.getString(R.string.take_picture)

            }

            dispatchTakePictureIntent()
        }

        // setup previous answer
        val previousAnswer = getQuestionDefinition().answerData

        if (previousAnswer != null) {
            currentPhotoPath = previousAnswer.response
            setPictureInPreview()
            captureBtn.text = context.getString(R.string.retake_picture)
        }

        addAnswerView(layout)
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

            takePictureIntent.resolveActivity(context.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }

                Timber.d("Image result from creation: $photoFile")
                // Continue only if the File was successfully created
                photoFile?.also {

                    val photoURI: Uri = FileProvider.getUriForFile(
                        context,
                        "com.keronei.fileprovider",
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