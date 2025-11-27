package com.seenu.dev.android.lazypizza.domain.repository

import com.seenu.dev.android.lazypizza.data.repository.UserRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.UserInfo
import dev.gitlive.firebase.auth.auth

class UserRepositoryImpl : UserRepository {

    private val firebaseAuth = Firebase.auth

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun loginAsGuest(): FirebaseUser? {
        val result = firebaseAuth.signInAnonymously()
        return result.user
    }

}