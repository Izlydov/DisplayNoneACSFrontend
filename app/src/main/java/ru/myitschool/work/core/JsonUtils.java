package ru.myitschool.work.core;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

public class JsonUtils {
    private static final @NonNull Gson _gson = _createGson();

    @NonNull
    private static Gson _createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat(Constants.DATE_FORMAT);
        return gsonBuilder.create();
    }

    @NonNull
    public static Gson getGson() {
        return _gson;
    }
}
