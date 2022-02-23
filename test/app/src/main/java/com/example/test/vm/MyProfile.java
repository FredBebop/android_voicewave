package com.example.test.vm;

public class MyProfile {
    int mUid=0;
    String mChannel;
    String myNickName;
    boolean broadcaster=false;

    public void reset() {
        mChannel = "mChannel";
    }

    public int getmUid() {
        return mUid;
    }

    public String getmChannel() {
        return mChannel;
    }

    public String getMyNickName() {
        return myNickName;
    }

    public boolean isBroadcaster() {
        return broadcaster;
    }

    public void setmChannel(String mChannel) {
        this.mChannel = mChannel;
    }

    public void setmUid(int mUid) {
        this.mUid = mUid;
    }

    public void setMyNickName(String myNickName) {
        this.myNickName = myNickName;
    }

    public void setBroadcaster(boolean broadcaster) {
        this.broadcaster = broadcaster;
    }
}
