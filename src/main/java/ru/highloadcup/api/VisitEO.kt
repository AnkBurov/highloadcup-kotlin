package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

import javax.validation.constraints.Max
import javax.validation.constraints.Min

class VisitEO @JsonCreator constructor(props: Map<String, Any?>) {

    @JsonProperty("location")
    var location: Int? = null

    @JsonProperty("user")
    var user: Int? = null

    @Min(946684800L)
    @Max(1420070400L)
    @JsonProperty("visited_at")
    var visited_at: Long? = null

    @Min(0)
    @Max(5)
    @JsonProperty("mark")
    var mark: Int? = null

    init {
        props.values.forEach { if (it == null) throw IllegalArgumentException() }
        this.location = props["location"] as Int?
        this.user = props["user"] as Int?
        val visited_at = props["visited_at"]
        if (visited_at != null) {
            this.visited_at = java.lang.Long.valueOf(visited_at.toString())
        }
        this.mark = props["mark"] as Int?
    }
}
