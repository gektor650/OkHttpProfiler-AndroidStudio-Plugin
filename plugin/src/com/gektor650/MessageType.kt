package com.gektor650

enum class MessageType(val api: String) {
    INITIAL("I"),
    HEADER("H"),
    BODY("B"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromString(type: String): MessageType {
            for (value in values()) {
                if (type == value.api) {
                    return value
                }
            }
            return UNKNOWN
        }
    }
}