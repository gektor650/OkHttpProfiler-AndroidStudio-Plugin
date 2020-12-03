/**
 * Copyright 2018 LocaleBro.com [Ievgenii Tkachenko(gektor650@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itkacher.data

import com.itkacher.Resources
import java.text.SimpleDateFormat
import java.util.*

data class DebugRequest(val id: String) {
    var url: String? = null
    var method: String? = null
    val requestHeaders = ArrayList<String>()
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
    val responseHeaders = ArrayList<String>()
    private val responseBody = StringBuilder()
    var errorMessage: String? = null

    private val trash = StringBuilder()
    private var isRequestBodyLimitAchieved = false
    private var isResponseBodyLimitAchieved = false

    fun addRequestHeader(header: String) {
        requestHeaders.add(header)
    }

    fun addRequestBody(bodyPart: String) {
        if (!isRequestBodyLimitAchieved && requestBody.length < MAX_BODY_LENGTH) {
            requestBody.append(bodyPart)
        } else if(!isRequestBodyLimitAchieved){
            requestBody.clear()
            requestBody.append(Resources.getString("max_length"))
            isRequestBodyLimitAchieved = true
        }
    }

    fun addResponseHeader(header: String) {
        responseHeaders.add(header)
    }

    fun addResponseBody(bodyPart: String) {
        if (!isResponseBodyLimitAchieved && responseBody.length < MAX_BODY_LENGTH) {
            responseBody.append(bodyPart)
        } else if(!isResponseBodyLimitAchieved){
            responseBody.clear()
            responseBody.append(Resources.getString("max_length"))
            isResponseBodyLimitAchieved = true
        }
    }

    fun trash(message: String) {
        trash.append(message)
    }

    fun getRequestBodyString(): String {
        return requestBody.toString()
    }

    fun getResponseBodyString(): String {
        return responseBody.toString()
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
        return code?.compareTo(400) == 1 || isClosed && errorMessage != null
    }

    fun isValid(): Boolean {
        return method != null
    }

    companion object {
        const val SPACE = " "
        const val NEW_LINE = "\r\n"
        const val MAX_BODY_LENGTH = 300_000
    }
}
