package com.example.test.network;


public class Channels {
    private Channel[] channels;

    private int total_size;

    public Channel[] getChannels() {
        return channels;
    }

    public int getTotal_size() {
        return total_size;
    }

    public void setChannels(Channel[] channels) {
        this.channels = channels;
    }

    public void setTotal_size(int total_size) {
        this.total_size = total_size;
    }

    public static class Channel{
        private int user_count;
        private String channel_name;

        public int getUser_count() {
            return user_count;
        }

        public String getChannel_name() {
            return channel_name;
        }

        public void setChannel_name(String channel_name) {
            this.channel_name = channel_name;
        }

        public void setUser_count(int user_count) {
            this.user_count = user_count;
        }

    }
}
