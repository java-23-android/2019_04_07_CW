package com.sheygam.java_23_07_04_19_cw;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpProvider {
    public static final String BASE_URL = "https://contacts-telran.herokuapp.com";
    private static final HttpProvider ourInstance = new HttpProvider();
    private OkHttpClient client;
    private Gson gson;
    private final MediaType JSON = MediaType.parse("application/json; charset=UTF-8");

    public static HttpProvider getInstance() {
        return ourInstance;
    }

    private HttpProvider() {
//        client = new OkHttpClient();
        client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15,TimeUnit.SECONDS)
                .build();
        gson = new Gson();
    }

    public String registration(String email, String password) throws IOException {
        AuthRequestDto requestDto = new AuthRequestDto(email,password);
        String jsonBody = gson.toJson(requestDto);

        RequestBody body = RequestBody.create(JSON,jsonBody);

        Request request = new Request.Builder()
                .url(BASE_URL +"/api/registration")
                .post(body)
//                .addHeader("Authorization","token")
                .build();

        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            String responseJson = response.body().string();
            AuthResponseDto responseDto =
                    gson.fromJson(responseJson,AuthResponseDto.class);
            return responseDto.token;
        }else if(response.code() == 409 || response.code() == 400){
            String responseJson = response.body().string();
            ErrorResponseDto error =
                    gson.fromJson(responseJson,ErrorResponseDto.class);
            throw new RuntimeException(error.message);
        }else{
            String responseJson = response.body().string();
            Log.d("HttpProvider", "registration error: " + responseJson);
            throw new RuntimeException("Server error! Call to support!");
        }
    }
}
