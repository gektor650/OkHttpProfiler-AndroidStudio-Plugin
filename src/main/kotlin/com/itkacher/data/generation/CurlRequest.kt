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
package com.itkacher.data.generation

import com.itkacher.data.DebugRequest
import java.lang.StringBuilder

class CurlRequest(private val debugRequest: DebugRequest) {

    override fun toString(): String {
        val builder = StringBuilder()
        builder
                .append(CURL)
                .append(SPACE)
        debugRequest.method?.let {
            builder
                    .append(METHOD)
                    .append(SPACE)
                    .append(it.toUpperCase())
                    .append(SPACE)
        }
        debugRequest.requestHeaders.forEach { header ->
            builder
                    .append(HEADER_PARAM)
                    .append(SPACE)
                    .append(STRING_WRAPPER)
                    .append(header)
                    .append(STRING_WRAPPER)
                    .append(SPACE)
        }
        val requestBodyString = debugRequest.getRequestBodyString()
        if (requestBodyString.isNotEmpty()) {
            builder
                    .append(DATA_BINARY)
                    .append(SPACE)
                    .append(STRING_WRAPPER)
                    .append(requestBodyString)
                    .append(STRING_WRAPPER)
                    .append(SPACE)
        }

        builder.append(COMPRESSED)
        builder.append(SPACE)
        builder.append(STRING_WRAPPER)
        builder.append(debugRequest.url)
        builder.append(STRING_WRAPPER)

        return builder.toString()
    }

    companion object {
        const val CURL = "curl"
        const val HEADER_PARAM = "-H"
        const val METHOD = "-X"
        const val DATA_BINARY = "--data-binary"
        const val COMPRESSED = "--compressed"
        const val STRING_WRAPPER = "'"
        const val SPACE = ' '
    }

}