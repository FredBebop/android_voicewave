package com.example.test;

import android.app.Application;

import com.example.test.network.MysqlService;
import com.example.test.rtm.RtmChatManager;
import com.example.test.vm.MyProfile;

public class AGApplication extends Application {

    private AgoraThread agoraThread;

    private RtmChatManager rtmChatManager;

    private MysqlService mysqlService =new MysqlService();

    private MyProfile myProfile =new MyProfile();

    public synchronized void init() {

        if (agoraThread == null) {
            if(rtmChatManager==null){
                rtmChatManager=new RtmChatManager(this);
                rtmChatManager.init();
            }
            agoraThread = new AgoraThread(getApplicationContext(),myProfile,rtmChatManager);
            agoraThread.start();
            agoraThread.waitForReady();
        }
    }

    public synchronized AgoraThread getAgoraThread() {
        return agoraThread;
    }

    public MyProfile getMyProfile() {
        return myProfile;
    }

    public MysqlService getMysqlService() {
        return mysqlService;
    }
}
