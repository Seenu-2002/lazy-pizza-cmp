package com.seenu.dev.android.lazypizza.domain.util

expect class PhoneNumberValidator {

    fun isValid(phoneNumber: String): Boolean

    fun format(phoneNumber: String): String

}