package com.seenu.dev.android.lazypizza

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.seenu.dev.android.lazypizza.firebase.LazyPizzaAuth
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    private val auth by lazy {
        LazyPizzaAuth(WeakReference(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        Firebase.initialize(this)
        super.onCreate(savedInstanceState)

        setContent {
            LazyPizzaApp(auth = auth)
        }
    }
}