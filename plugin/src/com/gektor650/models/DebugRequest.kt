package com.gektor650.models

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.JsonElement
import gherkin.deps.com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.StringBuilder
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
    var isClosed = false
    private val responseHeaders = ArrayList<String>()
    private val responseBody = StringBuilder()
    var requestContentType: ContentType? = null
    var responseContentType: ContentType? = null

    private val trash = StringBuilder()

    fun addRequestHeader(header: String) {
        if(requestContentType == null) {
            requestContentType = getContentType(header)
        }
        requestHeaders.add(header)
    }

    fun addRequestBody(bodyPart: String) {
        requestBody.append(bodyPart)
    }

    fun addResponseHeader(header: String) {
        if(responseContentType == null) {
            responseContentType = getContentType(header)
        }
        responseHeaders.add(header)
    }

    fun addResponseBody(bodyPart: String) {
        responseBody.append(bodyPart)
    }

    fun trash(message: String) {
        trash.append(message)
    }

    fun getContentType(headerString: String): ContentType? {
        val headerLowerCase = headerString.toLowerCase()
        for (value in ContentType.values()) {
            for (type in value.types) {
                if(headerLowerCase == type) {
                    return value
                }
            }
        }
        return null
    }

    fun getResponseJsonNode(): JsonNode? {
        val objectMapper = ObjectMapper()
        return objectMapper.readTree(responseBody.toString())
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
        isClosed = true
    }

    companion object {
        const val SPACE = " "
        const val NEW_LINE = "\r\n"
    }
}
