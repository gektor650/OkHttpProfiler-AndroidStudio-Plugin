package com.gektor650.models

enum class ContentType(vararg val types: String) {
    JSON("content-type: application/json", "content-type: text/json"),
    UNKNOWN()
}