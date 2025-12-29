package com.cda.presentation.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatInstant(instant: Instant): String {
    val fmt = DateTimeFormatter.ofPattern("dd MM yyyy").withZone(ZoneId.systemDefault())
    return fmt.format(instant)
}


