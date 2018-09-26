package com.gektor650.models

import java.lang.StringBuilder

data class DebugRequest(val id: Long) {
    private var url: String? = null
    private val headers = HashMap<String, String>()
    private val bodyBuilder = StringBuilder()
    private val trash = StringBuilder()

    fun addUrl(url: String?) {
        this.url = url
    }

    fun addHeader(header: String) {

    }

    fun addBody(bodyPart: String) {
        bodyBuilder.append(bodyPart)
    }

    fun trash(message: String) {
        trash.append(message)
    }
}
