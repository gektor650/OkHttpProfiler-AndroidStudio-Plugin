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
package com.itkacher.views

enum class Tabs(val resName: String) {
    TAB_REQUEST_RAW("tab_raw_request"),
    TAB_RESPONSE_RAW("tab_raw_response"),
    TAB_REQUEST_JSON("tab_json_request"),
    TAB_RESPONSE_JSON("tab_json_response"),
    TAB_REQUEST_FORMATTED("tab_request_formatted"),
    TAB_RESPONSE_FORMATTED("tab_response_formatted"),
    TAB_REQUEST_HEADERS("tab_request_headers"),
    TAB_RESPONSE_HEADERS("tab_response_headers"),
    TAB_ERROR_MESSAGE("tab_error_message")
}