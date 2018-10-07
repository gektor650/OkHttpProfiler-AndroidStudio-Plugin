package com.gektor650.okhttp_profiler_interceptor.transfer;

import android.util.Log;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class LogDataTransfer implements DataTransfer {
    private static final int LOG_LENGTH = 255;
    private static final String LOG_PREFIX = "OKPRFL";
    private static final String DELIMITER = "_";
    private static final String HEADER_DELIMITER = ":";

    @Override
    public void sendRequest(String id, Request request) {
        log(id, MessageType.REQUEST_METHOD, request.method());
        String url = request.url().toString();
        log(id, MessageType.REQUEST_URL, url);
        log(id, MessageType.REQUEST_TIME, String.valueOf(System.currentTimeMillis()));
        Headers headers = request.headers();
        if (headers != null) {
            for (String name : headers.names()) {
                log(id, MessageType.REQUEST_HEADER, name + ":" + headers.get(name));
            }
        }

        RequestBody body = request.body();
        if (body != null) {
            largeLog(id, MessageType.REQUEST_BODY, body.toString());
        }
    }

    @Override
    public void sendResponse(String id, Response response) {
        Headers headers = response.headers();
        log(id, MessageType.RESPONSE_STATUS, String.valueOf(response.code()));
        if (headers != null) {
            for (String name : headers.names()) {
                log(id, MessageType.RESPONSE_HEADER, name + HEADER_DELIMITER + headers.get(name));
            }
        }
        if (response.body() != null) {
            try {
                largeLog(id, MessageType.RESPONSE_BODY, response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendDuration(String id, long duration) {
        log(id, MessageType.RESPONSE_TIME, String.valueOf(duration));
        log(id, MessageType.RESPONSE_END, "-->");
    }

    private void log(String id, MessageType type, String message) {
        Log.d(LOG_PREFIX + DELIMITER + id + DELIMITER + type.name, message);
    }

    private void largeLog(String id, MessageType type, String content) {
        if (content.length() > LOG_LENGTH) {
            String part = content.substring(0, LOG_LENGTH);
            log(id, type, part);
            largeLog(id, type, content.substring(LOG_LENGTH));
        } else {
            log(id, type, content);
        }
    }
}
