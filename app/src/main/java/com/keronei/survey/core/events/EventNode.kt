package com.keronei.survey.core.events

data class EventNode<T>(var value: T, var next: EventNode<T>? = null) {
    override fun toString(): String {
        return if (next != null) {
            "$value -> $next"
        } else {
            "$value"
        }
    }
}
