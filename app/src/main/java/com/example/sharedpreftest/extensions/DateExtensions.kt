package com.example.sharedpreftest.extensions

import java.time.Instant
import java.time.ZoneId
import java.util.*
import java.time.ZonedDateTime

fun Date.toLocalDateTime(): ZonedDateTime = Instant.ofEpochMilli(this.time)
    .atZone(ZoneId.systemDefault())

fun Long.toLocalDateTime(): ZonedDateTime = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
