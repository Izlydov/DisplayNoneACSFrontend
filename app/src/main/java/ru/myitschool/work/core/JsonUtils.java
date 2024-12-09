package ru.myitschool.work.core;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final @NonNull Gson _gson = _createGson();

    @NonNull
    private static Gson _createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        return gsonBuilder.create();
    }

    @NonNull
    public static Gson getGson() {
        return _gson;
    }
}
