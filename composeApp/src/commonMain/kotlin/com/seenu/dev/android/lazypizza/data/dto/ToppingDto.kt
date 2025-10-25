package com.seenu.dev.android.lazypizza.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ToppingDto constructor(
    val id: Long,
    val name: String,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String,
)