package com.gektor650.okhttp_profiler_interceptor;

import android.util.Log;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author gektor650
 * @since 9/25/18
 */
public class OkHttpProfilerInterceptor implements Interceptor {

    private static final AtomicLong idsGenerator = new AtomicLong();
    private static final String LOG_PREFIX = "_OKPRFL";
    private static final String DELIMITER = ":";
    public static final int LOG_LENGTH = 256;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Profiler profiler = new Profiler(chain.request());
        profiler.printInitial();
        Response response = chain.proceed(chain.request());
        profiler.printResponse(response);
        return response;
    }

    class Profiler {
        private final Long id = idsGenerator.incrementAndGet();
        private final String url;
        private final Headers headers;
        private final RequestBody body;
        private final String method;

        Profiler(Request request) {
            method = request.method();
            url = request.url().toString();
            headers = request.headers();
            body = request.body();
        }

        private void printInitial() {
            Log.d(LOG_PREFIX, id + DELIMITER + MessageType.INITIAL.name + DELIMITER + method + " " + url);
        }

        private void printResponse(Response response) throws IOException {
            if (response.body() != null) {
                String body = response.body().string();
                largeLog(body);
            }
        }

        private void logBody(String message) {
            Log.d(LOG_PREFIX, id + DELIMITER + MessageType.RESPONSE_BODY.name + DELIMITER + message);
        }

        private void largeLog(String content) {
            if (content.length() > LOG_LENGTH) {
                String part = content.substring(0, LOG_LENGTH);
                logBody(part);
                largeLog(content.substring(LOG_LENGTH));
            } else {
                logBody(content);
            }
        }
    }
}
