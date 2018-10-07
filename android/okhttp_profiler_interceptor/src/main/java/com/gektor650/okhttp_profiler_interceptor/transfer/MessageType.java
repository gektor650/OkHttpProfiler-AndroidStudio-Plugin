package com.gektor650.okhttp_profiler_interceptor.transfer;

public enum MessageType {
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
    UNKNOWN("UNKNOWN");

    public final String name;

    MessageType(String name) {
        this.name = name;
    }
}
