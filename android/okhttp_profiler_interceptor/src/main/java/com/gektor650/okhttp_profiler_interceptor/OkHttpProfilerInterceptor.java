package com.gektor650.okhttp_profiler_interceptor;

import android.util.Log;

import java.io.IOException;
import java.util.UUID;

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

    @Override
    public Response intercept(Chain chain) throws IOException {
        Profiler profiler = new Profiler(chain.request());
        profiler.printInitial();
        Response response = chain.proceed(chain.request());
        profiler.printResponse(response);
        return response;
    }

    class Profiler {
        private final String id;
        private final String url;
        private final Headers headers;
        private final RequestBody body;
        private final String method;

        Profiler(Request request) {
            id = UUID.randomUUID().toString();
            method = request.method();
            url = request.url().toString();
            headers = request.headers();
            body = request.body();
        }

        private void printInitial() {
            Log.d(BuildConfig.LOG_PREFIX, method + " " + url);
        }

        private void printResponse(Response response) throws IOException {
            if (response.body() != null) {
                Log.d(BuildConfig.LOG_PREFIX, response.body().string());
            }
        }
    }
}
