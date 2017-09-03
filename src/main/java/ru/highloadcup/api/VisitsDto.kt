package ru.highloadcup.api

import java.io.Serializable
import java.util.ArrayList

class VisitsDto(val visits: List<Visit> = ArrayList()) : Serializable