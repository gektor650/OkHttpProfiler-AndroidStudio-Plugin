package com.gektor650.okhttp_profiler_interceptor;

import android.util.Log;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import okhttp3.*;

/**
 * @author gektor650
 * @since 9/25/18
 */
public class OkHttpProfilerInterceptor implements Interceptor {

    private static final AtomicLong idsGenerator = new AtomicLong();
    private static final String LOG_PREFIX = "OKPRFL";
    private static final String DELIMITER = "_";
    private static final String HEADER_DELIMITER = ":";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Profiler profiler = new Profiler(chain.request());
        Response response = chain.proceed(chain.request());
        profiler.printResponse(response);
        return response;
    }

    class Profiler {
        private final Long id = idsGenerator.incrementAndGet();

        Profiler(Request request) {
            String method = request.method();
            String url = request.url().toString();
            Headers headers = request.headers();
            log(MessageType.INITIAL, method + " " + url);
            if (headers != null) {
                for (String name : headers.names()) {
                    log(MessageType.REQUEST_HEADER, name + ":" + headers.get(name));
                }
            }

            RequestBody body = request.body();
            if (body != null) {
                log(MessageType.REQUEST_BODY, body.toString());
            }
        }

        private void printResponse(Response response) throws IOException {
            Headers headers = response.headers();
            if (headers != null) {
                for (String name : headers.names()) {
                    log(MessageType.RESPONSE_HEADER, name + HEADER_DELIMITER + headers.get(name));
                }
            }
            if (response.body() != null) {
                log(MessageType.RESPONSE_BODY, response.body().string());
            }
        }

        private void log(MessageType type, String message) {
            Log.d(LOG_PREFIX + DELIMITER + id + DELIMITER + type.name, message);
        }
    }
}
