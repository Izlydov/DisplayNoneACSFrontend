package ru.myitschool.work.core.components.employee;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Optional;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.myitschool.work.core.ApiUtils;
import ru.myitschool.work.core.Config;
import ru.myitschool.work.core.JsonUtils;
import ru.myitschool.work.core.components.employee.models.Employee;

public class EmployeeAuthManager {
    public static final String API_BASE = Config.SERVER_ADDRESS + "/api/";
    private static final @NonNull OkHttpClient _client = ApiUtils.getOkHttpClient();

    public static boolean checkEmployeeAuth(String login) throws IOException {
        Request request = new Request.Builder().url(API_BASE + login + "/" + "auth").build();

        try (Response response = _client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    public static Pair<Integer, Optional<Employee>> getEmployeeInfo(String login) {
        Request request = new Request.Builder().url(API_BASE + login + "/" + "info").build();

        try (Response response = _client.newCall(request).execute()) {
            Log.i("AA", "CODE" + response.code());
            if (!response.isSuccessful()) return new Pair<>(response.code(), Optional.empty());

            ResponseBody responseBody = response.body();
            if (responseBody == null) return new Pair<>(response.code(), Optional.empty());
            String body = responseBody.string();
            if (body.isEmpty()) return new Pair<>(response.code(), Optional.empty());
            Log.i("AA", "CODE" + body);

            return new Pair<>(response.code(), Optional.of(parseEmployee(body)));
        } catch (IOException e) {
            return new Pair<>(-1, Optional.empty());
        }
    }

    public static int openDoor(String login, long code) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("value", code);

        Request request = new Request.Builder().url(API_BASE + login + "/" + "open").patch(ApiUtils.createJsonRequestBody(body)).build();
        try (Response response = _client.newCall(request).execute()) {
            return response.code();
        }
    }

    public static Employee parseEmployee(String json) {
        return JsonUtils.getGson().fromJson(json, Employee.class);
    }
}
