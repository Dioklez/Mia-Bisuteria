package com.miabisuteri.admin.util

import java.text.NumberFormat
import java.util.Locale

fun Long.formatArs(): String {
    return "$" + NumberFormat.getNumberInstance(Locale("es", "AR")).format(this)
}

fun Double.formatArs(): String {
    return "$" + NumberFormat.getNumberInstance(Locale("es", "AR")).format(this.toLong())
}
