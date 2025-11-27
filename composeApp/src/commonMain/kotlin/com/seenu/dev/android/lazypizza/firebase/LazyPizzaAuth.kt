package com.seenu.dev.android.lazypizza.firebase

import dev.gitlive.firebase.FirebaseException
import dev.gitlive.firebase.auth.FirebaseAuthException
import dev.gitlive.firebase.auth.PhoneAuthCredential

expect class LazyPizzaAuth {

    fun login(phoneNumber: String, authListener: AuthListener)

    fun getCredentials(verificationId: String, otp: String): PhoneAuthCredential

    suspend fun verify(credential: dev.gitlive.firebase.auth.PhoneAuthCredential)

}

interface AuthListener {
    fun onCodeAutoRetrievalTimeOut(str: String)
    fun onCodeSent(str: String)
    fun onVerificationCompleted(cred: PhoneAuthCredential)
    fun onVerificationFailed(e: FirebaseException)
}
