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

class RequestDataSource {

    companion object {

        private val requestMapByClients = HashMap<String, ArrayList<DebugRequest>>()
        private val requestMapById = LinkedHashMap<String, DebugRequest>()

        fun logMessage(id: String, type: MessageType, message: String): DebugRequest? {
            try {
                if (type != MessageType.UNKNOWN) {
                    val request = requestMapById[id]
                    return if (request == null) {
                        val newRequest = DebugRequest(id)
                        log(type, newRequest, message)
                        requestMapById[id] = newRequest
                        newRequest
                    } else {
                        log(type, request, message)
                        request
                    }

                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
            return null
        }

        private fun log(messageType: MessageType, request: DebugRequest, message: String) {
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

        fun getRequestList(clientKey: String): List<DebugRequest> {
            return requestMapByClients[clientKey] ?: emptyList()
        }

        fun saveRequest(clientKey: String, debugRequest: DebugRequest) {
            val list = requestMapByClients[clientKey]
            if (list == null) {
                val newList = ArrayList<DebugRequest>()
                newList.add(debugRequest)
                requestMapByClients[clientKey] = newList
            } else {
                list.add(debugRequest)
            }
        }

        fun clear() {
            requestMapByClients.clear()
            requestMapById.clear()
        }
    }


}