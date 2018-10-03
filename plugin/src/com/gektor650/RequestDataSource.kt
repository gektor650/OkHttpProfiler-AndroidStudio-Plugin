package com.gektor650

import com.gektor650.models.DebugRequest

class RequestDataSource {

    companion object {
        private val requestMap = LinkedHashMap<Long, DebugRequest>()

        fun logMessage(id: Long, type: MessageType, message: String): DebugRequest? {
            try {
                if (type != MessageType.UNKNOWN) {
                    val request = requestMap[id]
                    return if (request == null) {
                        val newRequest = DebugRequest(id)
                        log(type, newRequest, message)
                        requestMap[id] = newRequest
                        newRequest
                    } else {
                        log(type, request, message)
                        request
                    }

                }
            } catch (e : NumberFormatException) {
                e.printStackTrace()
            }
            return null
        }

        private fun log(messageType: MessageType, request: DebugRequest, message: String) {
            when (messageType) {
                MessageType.REQUEST_URL -> request.url = message
                MessageType.REQUEST_METHOD -> request.method = message
                MessageType.REQUEST_BODY -> request.addRequestBody(message)
                MessageType.REQUEST_HEADER -> request.addRequestHeader(message)
                MessageType.REQUEST_END -> request.addRequestHeader(message)
                MessageType.RESPONSE_TIME -> request.duration = message.toLong()
                MessageType.RESPONSE_STATUS -> request.status = message
                MessageType.RESPONSE_HEADER -> request.addResponseHeader(message)
                MessageType.RESPONSE_BODY -> request.addResponseBody(message)
                MessageType.RESPONSE_END -> request.closeResponse()
                else -> {
                    request.trash(message)
                }
            }
        }

    }


}