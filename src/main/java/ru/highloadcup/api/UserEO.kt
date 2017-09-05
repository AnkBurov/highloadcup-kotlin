package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonCreator

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class UserEO @JsonCreator constructor(props: Map<String, Any?>) {

    @Size(max = 100)
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    var email: String? = null

    @Size(max = 50)
    var firstName: String? = null

    @Size(max = 50)
    var lastName: String? = null

    var gender: User.Gender? = null

    @Min(value = -1262304000L)
    @Max(value = 915148800L)
    var birthDate: Long? = null

    init {
        props.values.forEach { if (it == null) throw IllegalArgumentException() }
        this.email = props["email"] as String?
        this.firstName = props["first_name"] as String?
        this.lastName = props["last_name"] as String?
        val gender = props["gender"] as String?
        if (gender != null) {
            this.gender = User.Gender.valueOf(gender)
        }
        val birth_date = props["birth_date"]
        if (birth_date != null) {
            this.birthDate = java.lang.Long.valueOf(birth_date.toString())
        }
    }
}
