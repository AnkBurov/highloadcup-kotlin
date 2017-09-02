package ru.highloadcup.dao

import org.jooq.Condition


inline fun <T> doIfValueNotNull(value: T?, callBack: (T) -> Unit) {
    if (value != null) callBack(value)
}

inline fun <T> T?.doIfNotNull(callBack: (T) -> Unit) {
    if (this != null) callBack(this)
}