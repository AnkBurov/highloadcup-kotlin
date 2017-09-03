package ru.highloadcup.api

import java.io.Serializable
import java.util.ArrayList

class UserVisitsDto(val visits: List<UserVisit> = ArrayList()) : Serializable
