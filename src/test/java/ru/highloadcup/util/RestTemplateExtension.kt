package ru.highloadcup.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun <T> ResponseEntity<T>.ok(): ResponseEntity<T> {
    assertEquals(HttpStatus.OK, this.statusCode)
    return this
}

fun <T> ResponseEntity<T>.bodyNotNull(): T {
    assertNotNull(this.body)
    return this.body
}