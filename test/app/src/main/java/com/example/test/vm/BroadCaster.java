package com.example.test.vm;

public class BroadCaster {
    private String uid; //agora uid
    private String uname; //用户名
    private boolean OpenAudio; //是否打开音频

    public BroadCaster(String uid,String uname,boolean OpenAudio){
        this.uid=uid;
        this.uname=uname;
        this.OpenAudio=OpenAudio;
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

    public boolean isOpenAudio() {
        return OpenAudio;
    }
}
