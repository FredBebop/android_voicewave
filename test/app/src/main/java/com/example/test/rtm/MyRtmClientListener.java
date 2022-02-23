package com.example.test.rtm;

import android.util.Log;

import java.util.Map;

import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class MyRtmClientListener implements RtmClientListener {
    @Override
    public void onConnectionStateChanged(int i, int i1) {
        switch (i) {
            case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING:
                Log.d("test","正在尝试网络重连中...");
                break;
            case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED:
                Log.d("test","断开重连，请重新登陆。");
                break;
        }
    }

    //回调接受到信息
    @Override
    public void onMessageReceived(RtmMessage rtmMessage, String s) {
        Log.d("fredbebop","用户 "+s+"说："+rtmMessage.getText());
    }

    @Override
    public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

    }

    @Override
    public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

    }

    @Override
    public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

    }
}
