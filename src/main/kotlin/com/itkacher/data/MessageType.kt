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