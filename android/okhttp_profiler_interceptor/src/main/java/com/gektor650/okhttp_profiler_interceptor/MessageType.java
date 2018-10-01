package com.gektor650.okhttp_profiler_interceptor;

enum MessageType {
    INITIAL("INL"),
    REQUEST_HEADER("RQH"),
    REQUEST_BODY("RQB"),
    REQUEST_END("RQD"),
    RESPONSE_HEADER("RSH"),
    RESPONSE_BODY("RSB"),
    RESPONSE_END("RSD"),
    UNKNOWN("UNKNOWN");

    public final String name;

    MessageType(String name) {
        this.name = name;
    }
}
