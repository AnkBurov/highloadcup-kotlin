package ru.highloadcup.util

import org.junit.Assert.assertEquals
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> ResponseEntity<T?>.ok(message: String? = null): ResponseEntity<T?> {
    assertEquals(message, HttpStatus.OK, this.statusCode)
    return this
}

fun <T> ResponseEntity<T?>.bodyNotNull(message: String? = null) = this.body ?: throw AssertionError(message)