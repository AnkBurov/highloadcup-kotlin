package ru.highloadcup.api

import com.fasterxml.jackson.annotation.JsonCreator

import javax.validation.constraints.Max
import javax.validation.constraints.Min

class VisitEO @JsonCreator constructor(props: Map<String, Any?>) {

    var locationId: Int? = null

    var userId: Int? = null

    @Min(946684800L)
    @Max(1420070400L)
    var visitedAt: Long? = null

    @Min(0)
    @Max(5)
    var mark: Int? = null

    init {
        props.values.forEach { if (it == null) throw IllegalArgumentException() }
        this.locationId = props["location"] as Int?
        this.userId = props["user"] as Int?
        val visited_at = props["visited_at"]
        if (visited_at != null) {
            this.visitedAt = java.lang.Long.valueOf(visited_at.toString())
        }
        this.mark = props["mark"] as Int?
    }
}
