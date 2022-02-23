package com.example.test.network;

public class Users {
    private boolean channel_exist;
    private int mode;
    private String[] broadcasters;
    private String[] audience;
    private int audience_total;

    public int getMode() {
        return mode;
    }

    public int getAudience_total() {
        return audience_total;
    }

    public String[] getAudience() {
        return audience;
    }

    public String[] getBroadcasters() {
        return broadcasters;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setChannel_exist(boolean channel_exist) {
        this.channel_exist = channel_exist;
    }

    public void setAudience(String[] audience) {
        this.audience = audience;
    }

    public void setAudience_total(int audience_total) {
        this.audience_total = audience_total;
    }

    public void setBroadcasters(String[] broadcasters) {
        this.broadcasters = broadcasters;
    }

    public boolean isChannel_exist() {
        return channel_exist;
    }
}
