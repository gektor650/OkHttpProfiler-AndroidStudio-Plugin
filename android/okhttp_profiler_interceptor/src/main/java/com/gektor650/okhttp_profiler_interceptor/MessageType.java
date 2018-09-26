package com.gektor650.okhttp_profiler_interceptor;

enum MessageType {
    INITIAL("INL"),
    REQUEST_HEADER("RQH"),
    REQUEST_BODY("RQB"),
    RESPONSE_HEADER("RSH"),
    RESPONSE_BODY("RSB"),
    UNKNOWN("UNKNOWN");

    public final String name;

    MessageType(String name) {
        this.name = name;
    }
}
