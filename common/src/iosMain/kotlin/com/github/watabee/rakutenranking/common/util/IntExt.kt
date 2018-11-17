package com.github.watabee.rakutenranking.common.util

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle

val currencyFormatter = NSNumberFormatter().apply {
    numberStyle = NSNumberFormatterDecimalStyle
}

fun Int.toCurrencyString(): String =
    currencyFormatter.stringFromNumber(NSNumber(this)) ?: "$this"
