package com.seenu.dev.android.lazypizza.domain.util

import co.touchlab.kermit.Logger.Companion.e
import com.google.android.play.integrity.internal.n
import com.google.i18n.phonenumbers.PhoneNumberUtil

actual class PhoneNumberValidator {

    private val util = PhoneNumberUtil.getInstance()

    actual fun isValid(phoneNumber: String): Boolean {
        try {
        val number = util.parse(phoneNumber, "US")
        return util.isValidNumber(number)
        } catch (exp: Exception) {
            e(exp) {
                "PhoneNumberValidator:format: Exception while formatting phone number $phoneNumber"
            }
            return false
        }
    }

    actual fun format(phoneNumber: String): String {
        try {
            val number = util.parse(phoneNumber, "US")
            return util.format(number, PhoneNumberUtil.PhoneNumberFormat.E164)
        } catch (exp: Exception) {
            e(exp) {
                "PhoneNumberValidator:format: Exception while formatting phone number $phoneNumber"
            }
            return phoneNumber
        }
    }
}