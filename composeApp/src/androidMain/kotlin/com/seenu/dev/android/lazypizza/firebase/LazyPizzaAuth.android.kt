package com.seenu.dev.android.lazypizza.firebase

import android.app.Activity
import androidx.compose.runtime.Stable
import co.touchlab.kermit.Logger
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.lang.ref.WeakReference

@Stable
actual class LazyPizzaAuth constructor(
    private val activity: WeakReference<Activity>
) {

    actual fun login(phoneNumber: String, authListener: AuthListener) {
        val activity = activity.get() ?: run {
            Logger.e {
                "Activity reference lost. Cannot proceed with phone authentication."
            }
            return
        }

        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        Logger.d {
                            "Phone verification completed for phone number: $phoneNumber"
                        }
                        authListener.onVerificationCompleted(
                            dev.gitlive.firebase.auth.PhoneAuthCredential(
                                p0
                            )
                        )
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        Logger.d {
                            "Verification code sent to phone number: $phoneNumber"
                        }
                        val testCode = "556055" // the one you set in Firebase Console
                        val credential = PhoneAuthProvider.getCredential(verificationId, testCode)
                        FirebaseAuth.getInstance()
                            .signInWithCredential(credential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = task.result?.user
                                    Logger.d {
                                        "Test phone number sign-in successful for phone number: $phoneNumber -> user = ${user?.uid}"
                                    }
                                } else {
                                    Logger.e {
                                        "Test phone number sign-in failed for phone number: $phoneNumber, error: ${task.exception?.message}"
                                    }
                                }
                            }
                        authListener.onCodeSent(verificationId)
                    }

                    override fun onCodeAutoRetrievalTimeOut(p0: String) {
                        Logger.d {
                            "Code auto-retrieval timed out for phone number: $phoneNumber"
                        }
                        authListener.onCodeAutoRetrievalTimeOut(p0)
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Logger.e {
                            "Phone authentication failed: ${p0.message}"
                        }
                        authListener.onVerificationFailed(
                            FirebaseException(
                                p0.message ?: "Unknown error occurred during phone authentication."
                            )
                        )
                    }
                }
            )
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    actual fun getCredentials(
        verificationId: String,
        otp: String
    ): dev.gitlive.firebase.auth.PhoneAuthCredential {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        return dev.gitlive.firebase.auth.PhoneAuthCredential(credential)
    }

    actual suspend fun verify(credential: dev.gitlive.firebase.auth.PhoneAuthCredential) {
        return suspendCancellableCoroutine {
            FirebaseAuth.getInstance()
                .signInWithCredential(credential.android)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        Logger.d {
                            "Phone sign-in successful: user = ${user?.uid}"
                        }
                        it.resumeWith(Result.success(Unit))
                    } else {
                        val exception = task.exception
                        Logger.e {
                            "Phone sign-in failed: ${exception?.message}"
                        }
                        it.resumeWith(
                            Result.failure(
                                dev.gitlive.firebase.auth.FirebaseAuthException(
                                    "ERROR_UNKNOWN",
                                    exception?.message ?: "Unknown error occurred during sign-in.",
                                )
                            )
                        )
                    }
                }
        }
    }

}