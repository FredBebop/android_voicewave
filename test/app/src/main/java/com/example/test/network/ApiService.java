package com.example.test.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ApiService {

    @GET("/dev/v1/channel/user/{appid}/{channelName}")
    public Call<Response<Users>> getUsers(@Path("appid") String appid,
                                             @Path("channelName") String channelName,
                                             @Header("Authorization")  String authorizationHeader,
                                             @Header("Content-Type") String contentTypeHeader);
    @GET("/dev/v1/channel/{appid}")
    public Call<Response<Channels>> getChannels(@Path("appid") String appid,
                                                   @Header("Authorization")  String authorizationHeader,
                                                   @Header("Content-Type") String contentTypeHeader);
}
