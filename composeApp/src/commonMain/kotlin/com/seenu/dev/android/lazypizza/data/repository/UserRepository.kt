package com.seenu.dev.android.lazypizza.data.repository

import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.UserInfo

interface UserRepository {

    fun isUserLoggedIn(): Boolean
    suspend fun getUser(): FirebaseUser?
    suspend fun signOut()
    suspend fun loginAsGuest(): FirebaseUser?
}