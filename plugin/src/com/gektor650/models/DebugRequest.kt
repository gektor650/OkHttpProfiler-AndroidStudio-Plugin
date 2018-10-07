package com.gektor650.models

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

data class DebugRequest(val id: String) {
    var url: String? = null
    var method: String? = null
    private val requestHeaders = ArrayList<String>()
    private val requestBody = StringBuilder()
    var duration: String? = null
    var responseCode: Int? = null
    var requestTime: String? = null
        set(value) {
            field = if (value != null) {
                try {
                    SimpleDateFormat("HH:mm:ss").format(Date(value.toLong()))
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        }
    var isClosed = false
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

    fun getResponseJsonNode(): JsonNode? {
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.readTree(responseBody.toString())
        } catch (e: IOException) {
            null
        }
    }

    fun getRequestJsonNode(): JsonNode? {
        val objectMapper = ObjectMapper()
        return try {
            objectMapper.readTree(requestBody.toString())
        } catch (e: IOException) {
            null
        }
    }

    fun prettifyNode(jsonNode: JsonNode): String? {
        return try {
            val mapper = ObjectMapper()
            mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun toString(): String {
        return "$id $url $duration"
    }

    fun getRawRequest(): String? {
        return getRawDataString(requestHeaders, requestBody).toString()
    }

    fun getRawResponse(): String? {
        val data = getRawDataString(responseHeaders, responseBody)
        data.insert(0, SPACE)
        data.insert(0, responseCode)
        return data.toString()
    }

    private fun getRawDataString(headers: List<String>, body: StringBuilder): StringBuilder {
        val builder = StringBuilder()
        builder.append(method)
                .append(SPACE)
                .append(url)
                .append(NEW_LINE)
                .append(NEW_LINE)
        for (requestHeader in headers) {
            builder.append(requestHeader)
                    .append(NEW_LINE)
        }
        builder.append(NEW_LINE)
        builder.append(body)
        return builder
    }

    fun closeResponse() {
        isClosed = true
    }

    fun isFallenDown(): Boolean {
        val code = responseCode
        return code?.compareTo(400) == 1
    }

    companion object {
        const val SPACE = " "
        const val NEW_LINE = "\r\n"
    }
}
