package com.example.test.vm;

import androidx.annotation.Nullable;

public class RaisedHandsMember {
    private String userId;
    private String nickName;
    private boolean isInvited;
    private int userIcon;

    public RaisedHandsMember(String userId,String nickName,boolean isInvited){
        this.userId=userId;
        this.nickName=nickName;
        this.isInvited=isInvited;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj.getClass()== RaisedHandsMember.class){
            if(userId.equals(((RaisedHandsMember) obj).userId))
            return true;
        }
        return false;
    }

    public String getUserId() {
        return userId;
    }

    public String getNickName() {
        return nickName;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
