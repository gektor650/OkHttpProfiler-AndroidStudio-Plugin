package com.itkacher.views

enum class Tabs(val resName: String) {
    TAB_RAW_REQUEST("tab_raw_request"),
    TAB_RAW_RESPONSE("tab_raw_response"),
    TAB_JSON_REQUEST("tab_json_request"),
    TAB_JSON_RESPONSE("tab_json_response"),
    TAB_REQUEST_FORMATTED("tab_request_formatted"),
    TAB_RESPONSE_FORMATTED("tab_response_formatted"),
    TAB_REQUEST_HEADERS("tab_request_headers"),
    TAB_RESPONSE_HEADERS("tab_response_headers"),
    TAB_ERROR_MESSAGE("tab_error_message")
}