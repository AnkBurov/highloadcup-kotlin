package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size
import java.io.Serializable

data class User (
        val id: Int,

        @Size(max = 100)
        @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        var email: String,

        @Size(max = 50)
        @JsonProperty("first_name")
        val firstName: String,

        @Size(max = 50)
        @JsonProperty("last_name")
        val lastName: String,

        val gender: Gender,

        @Min(value = -1262304000L)
        @Max(value = 915148800L)
        @JsonProperty("birth_date")
        val birthDate: Long
) : Serializable {
    enum class Gender {
        m, f
    }
}
