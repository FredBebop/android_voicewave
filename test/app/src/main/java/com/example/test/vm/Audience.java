package com.example.test.vm;

public class Audience {
    private String uid; //agora uid
    private String uname; //用户名
    private boolean OpenAudio; //是否打开音频
    private boolean Invite; //是否发送邀请

    public Audience(String uid, String uname, boolean OpenAudio, boolean Invite){
        this.uid=uid;
        this.uname=uname;
        this.OpenAudio=OpenAudio;
        this.Invite=Invite;
    }


    public void setInvite(boolean invite) {
        Invite = invite;
    }

    public void setOpenAudio(boolean openAudio) {
        OpenAudio = openAudio;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public boolean isInvite() {
        return Invite;
    }

    public boolean isOpenAudio() {
        return OpenAudio;
    }
}
