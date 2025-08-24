package me.delyfss.medved.base.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Frame(
    val x: Float,
    val y: Float
)