package com.gektor650.models

import java.lang.StringBuilder
import java.util.*

data class DebugRequest(val id: Long) {
    private val startTime = Date().time
    private var duration: Long? = null
    private var url: String? = null
    private val requestHeaders = ArrayList<String>()
    private val requestBody = StringBuilder()

    private val responseHeaders = ArrayList<String>()
    private val responseBody = StringBuilder()

    private val trash = StringBuilder()

    fun addUrl(url: String?) {
        this.url = url
    }

    fun addRequestHeader(header: String) {
        requestHeaders.add(header)
    }

    fun addRequestBody(bodyPart: String) {
        requestBody.append(bodyPart)
    }

    fun addResponseHeader(header: String) {
        responseHeaders.add(header)
    }

    fun addResponseBody(bodyPart: String) {
        responseBody.append(bodyPart)
    }

    fun trash(message: String) {
        trash.append(message)
    }

    override fun toString(): String {
        return "$id $url $duration"
    }

    fun closeRequest() {

    }

    fun closeResponse() {
        val endTime = Date().time
        duration = endTime - startTime
    }

    fun getRawRequest(): String? {
        val request = getRawDataString(requestHeaders, requestBody)
        request.insert(0, NEW_LINE)
        request.insert(0, url)
        return request.toString()
    }

    fun getRawResponse(): String? {
        return getRawDataString(responseHeaders, responseBody).toString()
    }

    private fun getRawDataString(headers: List<String>, body: StringBuilder): StringBuilder {
        val builder = StringBuilder()
        for (requestHeader in headers) {
            builder.append(requestHeader).append(NEW_LINE)
        }
        builder.append(NEW_LINE)
        builder.append(body)
        return builder
    }

    companion object {
        const val NEW_LINE = "\r\n"
    }
}
