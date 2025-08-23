package me.delyfss.medved.base.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseModel(
    val proba: Float
)
