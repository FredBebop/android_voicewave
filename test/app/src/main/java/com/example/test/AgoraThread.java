package com.example.test;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.test.rtm.MyRtmChannelListener;
import com.example.test.rtm.MyRtmClientListener;
import com.example.test.rtm.RtmChatManager;
import com.example.test.vm.MyProfile;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;

public class AgoraThread extends Thread {
    private final Context mContext;

    private static final int ACTION_WORKER_THREAD_QUIT = 0X1010;

    private static final int ACTION_WORKER_JOIN_CHANNEL = 0X2010;

    private static final int ACTION_WORKER_LEAVE_CHANNEL = 0X2011;

    private static final class AgoraThreadHandler extends Handler {
        private AgoraThread mAgoraThread;

        public AgoraThreadHandler(AgoraThread thread) {
            this.mAgoraThread = thread;
        }

        public void release() {
            mAgoraThread = null;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (mAgoraThread == null) {
                Log.d("AgoraThread", "handler is already released! " + msg.what);
                return;
            }

            switch (msg.what) {
                case ACTION_WORKER_THREAD_QUIT:
                    mAgoraThread.exit();
                    break;
                case ACTION_WORKER_JOIN_CHANNEL:
                    String[] data = (String[]) msg.obj;
                    mAgoraThread.joinChannel(data[0], msg.arg1,data[1]);
                    break;
                case ACTION_WORKER_LEAVE_CHANNEL:
                    String channel = (String) msg.obj;
                    mAgoraThread.leaveChannel(channel);
                    break;
            }
        }
    }

    private AgoraThreadHandler mAgoraHandler;

    private boolean mReady;

    private RtcEngine rtcEngine;

    private final EngineEventHandler mEngineEventHandler;

    private MyProfile myProfile;

    private RtmChatManager rtmChatManager;

    private RtmClient rtmClient;

    private RtmChannel mRtmChannel;


    public AgoraThread(Context context, MyProfile myProfile, RtmChatManager rtmChatManager) {
        this.mContext = context;
        this.myProfile = myProfile;
        this.rtmChatManager = rtmChatManager;
        this.rtmClient = this.rtmChatManager.getRtmClient();
        this.mEngineEventHandler = new EngineEventHandler(context, this.myProfile);
    }

    @Override
    public void run() {
        Looper.prepare();

        mAgoraHandler = new AgoraThreadHandler(this);

        initRtcEngine();

        mReady = true;

        Looper.loop();
    }

    public final void joinChannel(final String channel, int uid,String token) {
        if (Thread.currentThread() != this) {
            Log.d("AgoraThread", "joinChannel() —— working thred asynchronously " + channel + " " + uid);
            Message message = new Message();
            message.what = ACTION_WORKER_JOIN_CHANNEL;
            message.obj = new String[]{channel,token};
            message.arg1 = uid;
            mAgoraHandler.sendMessage(message);
        }
        initRtcEngine();
        rtcEngine.joinChannel(token, channel, "test", uid);
        myProfile.setmChannel(channel);

        Log.d("AgoraThread", "joinChannel " + channel + " " + uid);
    }

    public final void leaveChannel(String channel) {
        if (Thread.currentThread() != this) {
            Log.d("AgoraThread", "leaveChannel() - working thread asynchronously " + channel);
            Message message = new Message();
            message.what = ACTION_WORKER_LEAVE_CHANNEL;
            message.obj = channel;
            mAgoraHandler.sendMessage(message);
            return;
        }

        if (rtcEngine != null) {
            rtcEngine.leaveChannel();
        }

        myProfile.reset();
        Log.d("AgoraThread", "leavel channel " + channel);
    }

    public final void exit() {
        if (Thread.currentThread() != this) {
            Log.d("AgoraThread", "exit() - exit app thread asynchronously");
            mAgoraHandler.sendEmptyMessage(ACTION_WORKER_THREAD_QUIT);
            return;
        }
        mReady = false;
        Looper.myLooper().quit();
        mAgoraHandler.release();

    }

    public final void waitForReady() {
        while (!mReady) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("AgoraThread", "wait for " + AgoraThread.class.getSimpleName());
        }
    }

    //RTM实现
//1.点对点消息
//2.创建加入频道
//3.发送频道消息
    public void sendPeerMessage(String content, String uid) {

        final RtmMessage message = rtmClient.createMessage();
        message.setText(content);

        rtmClient.sendMessageToPeer(uid, message, rtmChatManager.getSendMessageOptions(), new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //do nothing
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                int errorCode = errorInfo.getErrorCode();
                switch (errorCode) {
                    case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_TIMEOUT:
                    case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_FAILURE:
                        Log.d("test", "消息发送失败。");
                        break;
                    case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_PEER_UNREACHABLE:
                        Log.d("test", "peer已经下线。");
                        break;
                    case RtmStatusCode.PeerMessageError.PEER_MESSAGE_ERR_CACHED_BY_SERVER:
                        Log.d("test", "消息已经被服务器缓存。");
                        break;
                }
            }
        });
    }

    public void createAndJoinChannel(String channel,MyRtmChannelListener myRtmChannelListener) {

        mRtmChannel = rtmClient.createChannel(channel,myRtmChannelListener);

        if (mRtmChannel == null) {
            Log.d("test","create channel fail!");
        }

        mRtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("fredbebop","加入频道成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d("fredbebop","加入频道失败");
            }
        });
    }

    public void sendChannelMessage(String content) {
        RtmMessage message=rtmClient.createMessage();
        message.setText(content);
        mRtmChannel.sendMessage(message, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("fredbebop","发送频道消息成功");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d("fredbebop","发送频道消息失败"+errorInfo.getErrorCode());
            }
        });
    }

    public void leaveAndReleaseChannel() {
        if (mRtmChannel != null) {
            mRtmChannel.leave(null);
            mRtmChannel.release();
            mRtmChannel = null;
        }
    }

    public final MyProfile getMyProfile() {
        return myProfile;
    }

    public EngineEventHandler getEventHandler() {
        return mEngineEventHandler;
    }

    public RtcEngine getRtcEngine() {
        return rtcEngine;
    }

    public RtmClient getRtmClient() {
        return rtmClient;
    }

    public RtmChatManager getRtmChatManager() {
        return rtmChatManager;
    }

    public RtmChannel getmRtmChannel() {
        return mRtmChannel;
    }

    private void initRtcEngine() {
        if (rtcEngine == null) {
            String appId = mContext.getString(R.string.app_id);
            try {
                rtcEngine = RtcEngine.create(mContext, appId, mEngineEventHandler.mRtcEventHandler);
            } catch (Exception e) {
                throw new RuntimeException("Need to check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
            }
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            rtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            rtcEngine.enableAudioVolumeIndication(200, 3, false);

            rtmClient.login(null, myProfile.getMyNickName(), new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Rtm_login", "login success!");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {

                    Log.d("Rtm_login", "login fail:" + errorInfo.getErrorCode());
                }
            });
        }
    }
}
