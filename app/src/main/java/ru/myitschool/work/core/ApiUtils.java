package ru.myitschool.work.core;

import com.google.gson.JsonObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class ApiUtils {
    private static final OkHttpClient _client = _createOkHttpClient();

    private static OkHttpClient _createOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static OkHttpClient getOkHttpClient() {
        return _client;
    }

    public static RequestBody createJsonRequestBody(String json) {
        return RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
    }

    public static RequestBody createJsonRequestBody(JsonObject json) {
        return createJsonRequestBody(json.toString());
    }
}
