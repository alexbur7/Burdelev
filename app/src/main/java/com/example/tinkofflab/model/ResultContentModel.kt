package com.example.tinkofflab.model

import com.example.tinkofflab.ContentModel
import com.google.gson.annotations.SerializedName

data class ResultContentModel(
    @SerializedName("result")
    val result:List<ContentModel>)