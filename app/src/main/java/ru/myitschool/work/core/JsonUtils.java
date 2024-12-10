package ru.myitschool.work.core;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    private static final @NonNull Gson _gson = _createGson();

    @NonNull
    private static Gson _createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(Config.DATE_FORMAT);
        return gsonBuilder.create();
    }

    @NonNull
    public static Gson getGson() {
        return _gson;
    }
}
