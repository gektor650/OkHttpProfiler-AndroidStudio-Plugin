package com.gektor650.models

import java.lang.StringBuilder
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

data class DebugRequest(val id: Long) {
    var url: String? = null
    var method: String? = null
    private val requestHeaders = ArrayList<String>()
    private val requestBody = StringBuilder()
    var duration: Long? = null
    var status: String? = null
    private val startTime = Date()

    private val responseHeaders = ArrayList<String>()
    private val responseBody = StringBuilder()

    private val trash = StringBuilder()

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

    fun getRawRequest(): String? {
        val request = getRawDataString(requestHeaders, requestBody)
        request.insert(0, NEW_LINE)
        request.insert(0, url)
        request.insert(0, SPACE)
        request.insert(0, method)
        return request.toString()
    }

    fun getRawResponse(): String? {
        return getRawDataString(responseHeaders, responseBody).toString()
    }

    fun getStartTimeString(): String? {
        return SimpleDateFormat("HH:mm:ss").format(startTime)
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

    fun closeResponse() {

    }

    companion object {
        const val SPACE = " "
        const val NEW_LINE = "\r\n"
    }
}
