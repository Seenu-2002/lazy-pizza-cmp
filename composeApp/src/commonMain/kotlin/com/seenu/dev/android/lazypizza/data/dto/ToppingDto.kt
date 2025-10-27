package com.seenu.dev.android.lazypizza.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ToppingDto constructor(
    @Transient
    val id: String = "",
    val name: String,
    val price: Double,
    @SerialName("image_url")
    val imageUrl: String,
)