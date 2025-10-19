package com.seenu.dev.android.lazypizza.core.formatter

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual class CurrencyFormatter {
    actual fun format(amount: Double): String {
        return "$" + NSString.stringWithFormat("%.2F", amount)
    }
}