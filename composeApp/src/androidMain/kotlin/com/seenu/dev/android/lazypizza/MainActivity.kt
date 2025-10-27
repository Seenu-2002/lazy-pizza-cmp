package com.seenu.dev.android.lazypizza

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.Firebase
import com.google.firebase.initialize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        Firebase.initialize(this)
        super.onCreate(savedInstanceState)

        setContent {
            LazyPizzaApp()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    LazyPizzaApp()
}