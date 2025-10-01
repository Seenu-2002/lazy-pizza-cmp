package com.seenu.dev.android.lazypizza

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform