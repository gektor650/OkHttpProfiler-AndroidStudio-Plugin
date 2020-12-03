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

import java.util.concurrent.locks.ReentrantLock

class RequestDataSource {

    companion object {

        private val requestMapById = HashMap<String, DebugRequest>()
        private val reentrantLock = ReentrantLock()

        fun getRequestFromMessage(id: String, type: MessageType, message: String): DebugRequest? {
            try {
                if (type != MessageType.UNKNOWN) {
                    try {
                        reentrantLock.lock()
                        val request = requestMapById[id]
                        return if (request == null) {
                            val newRequest = DebugRequest(id)
                            fillRequest(type, newRequest, message)
                            requestMapById[id] = newRequest
                            newRequest
                        } else {
                            fillRequest(type, request, message)
                            request
                        }
                    } finally {
                        reentrantLock.unlock()
                    }
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return null
        }

        private fun fillRequest(messageType: MessageType, request: DebugRequest, message: String) {
            when (messageType) {
                MessageType.REQUEST_URL -> request.url = message
                MessageType.REQUEST_METHOD -> request.method = message
                MessageType.REQUEST_TIME -> request.requestTime = message
                MessageType.REQUEST_BODY -> request.addRequestBody(message)
                MessageType.REQUEST_HEADER -> request.addRequestHeader(message)
                MessageType.REQUEST_END -> request.addRequestHeader(message)
                MessageType.RESPONSE_TIME -> request.duration = message
                MessageType.RESPONSE_STATUS -> {
                    try {
                        request.responseCode = message.toInt()
                    } catch (_: NumberFormatException) {
                    }
                }
                MessageType.RESPONSE_HEADER -> request.addResponseHeader(message)
                MessageType.RESPONSE_BODY -> request.addResponseBody(message)
                MessageType.RESPONSE_ERROR -> request.errorMessage = message
                MessageType.RESPONSE_END -> request.closeResponse()
                else -> {
                    request.trash(message)
                }
            }
        }

        fun clear() {
            requestMapById.clear()
        }
    }


}