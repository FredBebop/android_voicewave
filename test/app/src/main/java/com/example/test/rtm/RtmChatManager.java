package com.example.test.rtm;

import android.content.Context;

import com.example.test.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;

public class RtmChatManager {
    private Context mContext;
    private RtmClient mRtmClient;
    private SendMessageOptions mSendMessageOptions;
    private List<RtmClientListener> mListenerList = new ArrayList<>();
    private RtmMessagePool mMessagePool=new RtmMessagePool();

    public RtmChatManager(Context mContext) {
        this.mContext = mContext;
    }

    public void init(){
        String appID=mContext.getString(R.string.rtm_app_id);

        mSendMessageOptions = new SendMessageOptions();//全局option，决定当前是否支持离线消息

        try{
            mRtmClient=RtmClient.createInstance(mContext, appID, new RtmClientListener() {

                //SDK 与 Agora RTM 系统的连接状态发生改变回调
                @Override
                public void onConnectionStateChanged(int i, int i1) {
                    for (RtmClientListener rtmClientListener:mListenerList){
                        rtmClientListener.onConnectionStateChanged(i,i1);
                    }
                }

                //收到点对点消息回调

                @Override
                public void onMessageReceived(RtmMessage rtmMessage, String s) {
                    if(mListenerList.isEmpty()){
                        mMessagePool.insertOfflineMessage(rtmMessage,s);
                    }else{
                        for (RtmClientListener rtmClientListener:mListenerList){
                            rtmClientListener.onMessageReceived(rtmMessage,s);
                        }
                    }
                }

                //收到点对点图片消息回调

                @Override
                public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

                }

                //收到点对点文件消息回调

                @Override
                public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

                }

                //主动回调：上传进度回调

                @Override
                public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                }

                //主动回调：下载进度回调

                @Override
                public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

                }

                //(SDK 断线重连时触发）当前使用的 RTM Token 已超过 24 小时的签发有效期

                @Override
                public void onTokenExpired() {

                }

                //被订阅用户在线状态改变回调

                @Override
                public void onPeersOnlineStatusChanged(Map<String, Integer> map) {

                }
            });
        }catch (Exception e){
            throw new RuntimeException("You need to check the RTM init process.");
        }
    }

    public RtmClient getRtmClient() {
        return mRtmClient;
    }

    public void registerListener(RtmClientListener listener) {
        mListenerList.add(listener);
    }

    public void unregisterListener(RtmClientListener listener) {
        mListenerList.remove(listener);
    }

    public void enableOfflineMessage(boolean enabled) {
        mSendMessageOptions.enableOfflineMessaging = enabled;
    }

    public boolean isOfflineMessageEnabled() {
        return mSendMessageOptions.enableOfflineMessaging;
    }

    public SendMessageOptions getSendMessageOptions() {
        return mSendMessageOptions;
    }

    public List<RtmMessage> getAllOfflineMessages(String peerId) {
        return mMessagePool.getAllOfflineMessages(peerId);
    }

    public void removeAllOfflineMessages(String peerId) {
        mMessagePool.removeAllOfflineMessages(peerId);
    }
}
