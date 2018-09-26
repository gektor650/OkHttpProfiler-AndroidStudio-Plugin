package com.gektor650

import com.gektor650.models.DebugRequest

class RequestDataSource {

    companion object {

        // Format of request "{id(Long):{TYPE(MessageType)}:MESSAGE(String)}"
        private const val DELIMITER = ':'

        fun logMessage(message: String): DebugRequest? {
            val indexOfId = message.indexOf(DELIMITER)
            if (indexOfId > 0) {
                try {
                    val id = message.subSequence(0, indexOfId).toString().toLong()
                    val withoutId = message.subSequence(indexOfId + 1, message.length)
                    val indexOfType = withoutId.indexOf(DELIMITER)
                    val typeString = withoutId.subSequence(0, indexOfType)
                    val messageType = MessageType.fromString(typeString.toString())
                    val messageWithoutType = withoutId.subSequence(indexOfType + 1, withoutId.length).toString()
                    if (indexOfType > 0) {
                        val request = requestMap[id]
                        return if (request == null) {
                            val newRequest = DebugRequest(id)
                            log(messageType, newRequest, messageWithoutType)
                            requestMap[id] = newRequest
                            newRequest
                        } else {
                            log(messageType, request, messageWithoutType)
                            request
                        }

                    }
                } catch (e : NumberFormatException) {
                    e.printStackTrace()
                }
            }
            return null
        }

        private fun log(messageType: MessageType, request: DebugRequest, message: String) {
            when (messageType) {
                MessageType.REQUEST_BODY -> request.addBody(message)
                MessageType.REQUEST_HEADER -> request.addHeader(message)
                MessageType.INITIAL -> request.addUrl(message)
                else -> {
                    request.trash(message)
                }
            }
        }

        private val requestMap = LinkedHashMap<Long, DebugRequest>()
    }


}