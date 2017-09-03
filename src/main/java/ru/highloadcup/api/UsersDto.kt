package ru.highloadcup.api

import java.io.Serializable
import java.util.ArrayList

class UsersDto(val users: List<User> = ArrayList()) : Serializable
