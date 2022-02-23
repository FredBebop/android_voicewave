package com.example.test.ui;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.AGApplication;
import com.example.test.AGEventHandler;
import com.example.test.BaseActivity;
import com.example.test.R;
import com.example.test.UserAdapter;
import com.example.test.rtm.MyRtmChannelListener;
import com.example.test.vm.ChannelVm;
import com.lxj.xpopup.XPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;

public class ChannelRoom extends BaseActivity implements AGEventHandler {
    ChannelVm channelVm;
    List<RtmChannelMember> userList = new ArrayList<>();
    UserAdapter userAdapter;
    RecyclerView rvUser;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        setUserView(((AGApplication) getApplication()).getMyProfile().isBroadcaster());

        TextView nickName =findViewById(R.id.tv_user_name);
        nickName.setText(getMyNickName());

        rvUser = findViewById(R.id.rv_user);

        channelVm = new ViewModelProvider(ChannelRoom.this).get(ChannelVm.class);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        channelVm.getChannelmemberList();
                        channelVm.getUsersLiveData().observe(ChannelRoom.this, new Observer<List<RtmChannelMember>>() {
                            @Override
                            public void onChanged(List<RtmChannelMember> rtmChannelMembers) {
                                List<RtmChannelMember> users = rtmChannelMembers;
                                userList.clear();
                                userList.addAll(users);
                                userAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        };
        setupUserRv();

    }

    private void setupUserRv() {
        if (userAdapter == null) {
            userAdapter = new UserAdapter(ChannelRoom.this, ((AGApplication) getApplication()).getMysqlService(), userList);
            rvUser.setLayoutManager(new LinearLayoutManager(this));
            rvUser.setAdapter(userAdapter);
            rvUser.setItemAnimator(new DefaultItemAnimator());
            rvUser.setNestedScrollingEnabled(true);
        } else {
            userAdapter.notifyDataSetChanged();
        }
    }

    private void setUserView(boolean flag) {
        if (flag) {
            ImageView imageView = findViewById(R.id.iv_apply);
            imageView.setVisibility(View.GONE);
        } else {
            FrameLayout frameLayout = findViewById(R.id.fl_raised_hands);
            frameLayout.setVisibility(View.GONE);
        }
        Intent intent = getIntent();
        String channelName = intent.getStringExtra("channelName");
        TextView textView = findViewById(R.id.tv_room_topic);
        textView.setText(channelName);
        Log.d("fredbebop",channelName);
    }

    public void userNotification(boolean flag) {
        View redPoint = findViewById(R.id.view_raised_hands_tip);
        if (flag) {
            redPoint.setVisibility(View.VISIBLE);
        } else {
            redPoint.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initUIandEvent() throws UnsupportedEncodingException {
        getEvent().addEventHandler(this);

        Intent intent = getIntent();
        String channelName = intent.getStringExtra("channelName");
        String GBKChannnelName = URLEncoder.encode(channelName, "GBK");
        boolean flag = intent.getExtras().getBoolean("flag");
        String token =intent.getStringExtra("response");
        if (flag) {
            getRtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
        }
        getAgoraThread().joinChannel(GBKChannnelName, ((AGApplication) getApplication()).getMyProfile().getmUid(),token);
        getAgoraThread().createAndJoinChannel(GBKChannnelName, new MyRtmChannelListener() {
            @Override
            public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
                super.onMessageReceived(rtmMessage, rtmChannelMember);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userNotification(true);
                    }
                });

                channelVm.addRaisedHandsMember(rtmMessage,rtmChannelMember);
            }

            @Override
            public void onMemberCountUpdated(int i) {
                Message message = new Message();
                handler.sendMessage(message);
            }
        });

        getRtmChatManager().registerListener(new RtmClientListener() {
            @Override
            public void onConnectionStateChanged(int i, int i1) {

            }

            @Override
            public void onMessageReceived(RtmMessage rtmMessage, String s) {

                switch (rtmMessage.getText()){
                    case "ok":{
                        new InviteDialogFragment(channelVm,s).show(getSupportFragmentManager(), new InviteDialogFragment.OnDissmissCallback() {
                            @Override
                            public void dismiss() {
                                ImageView imageView=findViewById(R.id.iv_apply);
                                imageView.setSelected(false);
                            }
                        });
                        break;
                    }
                    case "accept":{
                        new TopTipDialog().showDialog(getSupportFragmentManager(),s+getString(R.string.accept));
                        getRtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
                        break;
                    }
                    case "refuse":{
                        new TopTipDialog().showDialog(getSupportFragmentManager(),s+getString(R.string.refuse));
                        break;
                    }
                }
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
        });
        //TODO绑定加入RTM信息频道
    }

    @Override
    protected void deInitUIandEvent() {
    }

    public static void startActivity(Context context, String channelName, boolean flag,String response) {
        Intent intent = new Intent(context, ChannelRoom.class);
        intent.putExtra("channelName", channelName);
        intent.putExtra("flag", flag);
        intent.putExtra("response",response);
        context.startActivity(intent);
    }

    //----------------------RTC回调-----------------------------------
    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    @Override
    public void onUserOffline(int uid, int reason) {

    }

    @Override
    public void onExtraCallback(int type, Object... data) {

    }

    @Override
    public void onUserJoined(int uid, int elapsed) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ChannelRoomList.startActivity(this);
        getAgoraThread().leaveChannel(getAgoraThread().getMyProfile().getmChannel());
        getAgoraThread().leaveAndReleaseChannel();
        finish();
    }

    public void micClick(View view) {
        RtcEngine rtcEngine = getRtcEngine();
        rtcEngine.muteLocalAudioStream(!view.isSelected());

        view.setSelected(!view.isSelected());
    }

    public void leaveChannel(View view) {
        ChannelRoomList.startActivity(this);
        getAgoraThread().leaveChannel(getAgoraThread().getMyProfile().getmChannel());
        getAgoraThread().leaveAndReleaseChannel();
        finish();
    }

    public void raisedHands(View view) {
        if (view.isSelected()) {

        } else {
            view.setSelected(!view.isSelected());
            int content = ((AGApplication) getApplication()).getMyProfile().getmUid();
            getAgoraThread().sendChannelMessage(String.valueOf(content));
            new TopTipDialog().showDialog(getSupportFragmentManager(),getString(R.string.raisedHandsTip));
        }
    }


    public void raisedHandsListClick(View view) {
        new XPopup.Builder(ChannelRoom.this).enableDrag(true)
                .asCustom(new RaisedHandsListPop(ChannelRoom.this, channelVm)).show();

        userNotification(false);
    }
}