package com.gektor650

enum class MessageType(val api: String) {
    INITIAL("INL"),
    REQUEST_HEADER("RQH"),
    REQUEST_BODY("RQB"),
    RESPONSE_HEADER("RSH"),
    RESPONSE_BODY("RSB"),
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