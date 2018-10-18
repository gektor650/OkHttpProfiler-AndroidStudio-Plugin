package com.itkacher.data

enum class MessageType(val api: String) {
    REQUEST_URL("RQU"),
    REQUEST_TIME("RQT"),
    REQUEST_METHOD("RQM"),
    REQUEST_HEADER("RQH"),
    REQUEST_BODY("RQB"),
    REQUEST_END("RQD"),
    RESPONSE_TIME("RST"),
    RESPONSE_STATUS("RSS"),
    RESPONSE_HEADER("RSH"),
    RESPONSE_BODY("RSB"),
    RESPONSE_END("RSD"),
    RESPONSE_ERROR("REE"),
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