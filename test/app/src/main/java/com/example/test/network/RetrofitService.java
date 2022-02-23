package com.example.test.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.agora.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    public static <T> T cteateService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
