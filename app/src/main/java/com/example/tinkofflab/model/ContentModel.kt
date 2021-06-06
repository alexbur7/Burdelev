package com.example.tinkofflab

import com.google.gson.annotations.SerializedName

data class ContentModel(
    @SerializedName("id")
    val id:String,
    @SerializedName("gifURL")
    val url:String)
