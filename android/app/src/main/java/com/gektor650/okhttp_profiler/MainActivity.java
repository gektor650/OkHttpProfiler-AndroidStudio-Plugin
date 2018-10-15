package com.gektor650.okhttp_profiler;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gektor650.okhttp_profiler_interceptor.OkHttpProfilerInterceptor;

import java.io.IOException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private int time = 10;
    private static final String GOOGLE_URL1 = "https://raw.githubusercontent.com/corysimmons/colors.json/master/colors.json";
    private static final String GOOGLE_URL2 = "https://docs.oracle.com/jaasdasdasdasdas";
    private static final String GOOGLE_URL3 = "https://api.stash.rentberry.com/v1/apartment/935597/";
    private Handler mHandler = new Handler();
    private OkHttpClient mClient = new OkHttpClient.Builder().addInterceptor(
            new OkHttpProfilerInterceptor()
    ).build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendRequest();
        tick();
    }

    private void tick() {
        mHandler.postDelayed(this::sendRequest, time);
        time *= time;
    }

    private void sendRequest() {
        String[] array = new String[]{GOOGLE_URL1, GOOGLE_URL2, GOOGLE_URL3};
        int random = new Random().nextInt(array.length);
        Request request = new Request.Builder()
                .url(array[random])
                .get()
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                tick();
            }
        });
    }
}
