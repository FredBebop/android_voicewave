package com.example.test;

import android.content.Context;
import android.util.Log;

import com.example.test.vm.MyProfile;

import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;

public class EngineEventHandler {

    private final MyProfile myProfile;

    private final Context mContext;

    public EngineEventHandler(Context context, MyProfile myProfile) {
        this.myProfile = myProfile;
        this.mContext = context;
    }

    private final ConcurrentHashMap<AGEventHandler, Integer> mEventHandlerList = new ConcurrentHashMap<>();

    public void addEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.put(handler, 0);
    }

    public void removeEventHandler(AGEventHandler handler) {
        this.mEventHandlerList.remove(handler);
    }

    final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            Log.d("EngineEventHandler","onUserJoined"+(uid & 0xFFFFFFFFL));
            for(AGEventHandler handler : mEventHandlerList.keySet()){
                handler.onUserJoined(uid,elapsed);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            Log.d("EngineEventHandler","onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason);
            for (AGEventHandler handler : mEventHandlerList.keySet()) {
                handler.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onRtcStats(RtcStats stats) {
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
        }

        @Override
        public void onLastmileQuality(int quality) {
            Log.d("EngineEventHandler","onLastmileQuality " + quality);
        }

        @Override
        public void onConnectionLost() {
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d("EngineEventHandler","onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
            for (AGEventHandler handler : mEventHandlerList.keySet()) {
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        @Override
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.d("EngineEventHandler","onRejoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed);
        }

        @Override
        public void onAudioRouteChanged(int routing) {
            Log.d("EngineEventHandler","onAudioRouteChanged " + routing);

            for (AGEventHandler handler : mEventHandlerList.keySet()) {
                handler.onExtraCallback(AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED, routing);
            }
        }



        @Override
        public void onWarning(int warn) {
        }

        @Override
        public void onError(int err) {
        }
    };
}
