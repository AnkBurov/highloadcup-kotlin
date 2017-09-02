package ru.highloadcup.api

import java.io.Serializable

class EmptyJson : Serializable {

    var dummyId: Int? = null

    companion object {

        val INSTANCE = EmptyJson()
    }
}
