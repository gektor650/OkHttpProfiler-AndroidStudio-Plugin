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