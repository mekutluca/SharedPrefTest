package com.example.sharedpreftest.extensions

import java.time.ZonedDateTime

fun ZonedDateTime.toEpochMillis() = this.toEpochSecond() * 1000L
