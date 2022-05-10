package com.keronei.survey.data.remote.models

import com.google.gson.annotations.SerializedName

data class ChoiceOptionResponse(
    val value: String,
    @SerializedName("display_text") val displayText: String
)
