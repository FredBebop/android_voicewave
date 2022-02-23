package com.example.test;

public interface AGEventHandler {
    void onJoinChannelSuccess(String channel, int uid, int elapsed);

    void onUserOffline(int uid, int reason);

    void onExtraCallback(int type, Object... data);

    void onUserJoined(int uid,int elapsed);

    int EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED = 18;
}
