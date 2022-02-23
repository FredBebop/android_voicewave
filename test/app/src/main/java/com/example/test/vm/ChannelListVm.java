package com.example.test.vm;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.test.R;
import com.example.test.network.Channels;
import com.example.test.network.MysqlService;
import com.example.test.network.Repository;
import com.example.test.network.Response;


public class ChannelListVm extends AndroidViewModel {
    private String curTopic = "";
    private String GBKTopic = "";
    private int curChannelType = 0;
    private MutableLiveData<Response<Channels>> channelsLiveData;
    private Repository repository;

    public ChannelListVm(@NonNull Application application) {
        super(application);
    }

    public void init() {
        if (channelsLiveData != null) {
            return;
        }
        repository = Repository.getInstance();

        channelsLiveData = repository.getChannels(getApplication().getString(R.string.app_id));
    }

    public LiveData<Response<Channels>> getChannelsLiveData() {
        return channelsLiveData;
    }

    public void refreshChannelsLiveData() {
        channelsLiveData = repository.getChannels(getApplication().getString(R.string.app_id));
    }

    public void setCurTopic(String curTopic) {
        this.curTopic = curTopic;
    }

    public void setCurChannelType(int curChannelType) {
        this.curChannelType = curChannelType;
    }

    public void setGBKTopic(String GBKTopic) {
        this.GBKTopic = GBKTopic;
    }

    public String getCurTopic() {
        return curTopic;
    }

    public int getCurChannelType() {
        return curChannelType;
    }

    public String getGBKTopic() {
        return GBKTopic;
    }
}
