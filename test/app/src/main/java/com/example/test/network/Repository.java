package com.example.test.network;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

public class Repository {
    private final String authorizationHeader;

    private final ApiService apiService;

    private Handler handler;

    private final MysqlService mysqlService=new MysqlService();

    private static Repository instance;

    public synchronized static Repository getInstance(){
        if (instance==null){
            instance=new Repository();
        }
        return instance;
    }

    public Repository() {
        this.apiService = RetrofitService.cteateService(ApiService.class);

        final String customerKey = "11e5128c70d3452ba3255d49a7bfd9e6";
        final String customerSecret = "302ff7bdf5e14979812c7286787e2136";
        String plainCredentials = customerKey + ":" + customerSecret;
        String base64Credentials = new String(Base64.getEncoder().encode(plainCredentials.getBytes()));
        this.authorizationHeader = "Basic " + base64Credentials;
    }

    public MutableLiveData<Response<Users>> getUsers(String appId, String channelName) {
        MutableLiveData<Response<Users>> usersData = new MutableLiveData<>();
        apiService.getUsers(appId, channelName, authorizationHeader, "application/json").enqueue(new Callback<Response<Users>>() {
            @Override
            public void onResponse(Call<Response<Users>> call, retrofit2.Response<Response<Users>> response) {
                if (response.isSuccessful()) {
                    usersData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Response<Users>> call, Throwable t) {
                usersData.postValue(null);
            }
        });
        return usersData;
    }

    public MutableLiveData<Response<Channels>> getChannels(String appId) {
        MutableLiveData<Response<Channels>> channelsData = new MutableLiveData<>();
        apiService.getChannels(appId, authorizationHeader, "application/json").enqueue(new Callback<Response<Channels>>() {
            @Override
            public void onResponse(Call<Response<Channels>> call, retrofit2.Response<Response<Channels>> response) {
                if (response.isSuccessful()) {
                    channelsData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Response<Channels>> call, Throwable t) {
                channelsData.postValue(null);
            }
        });
        return channelsData;
    }


}
