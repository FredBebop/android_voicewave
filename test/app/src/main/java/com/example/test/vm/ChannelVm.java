package com.example.test.vm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.test.AGApplication;
import com.example.test.AgoraThread;
import com.example.test.R;
import com.example.test.network.MysqlService;
import com.example.test.network.Repository;
import com.example.test.network.Response;
import com.example.test.network.Users;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.RtcEngine;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmMessage;

public class ChannelVm extends AndroidViewModel {
    MutableLiveData<List<RtmChannelMember>> usersLiveData=new MutableLiveData<>();
    MutableLiveData<List<RaisedHandsMember>> raisedHandsList =new MutableLiveData<>();
    MutableLiveData<Integer> mChannelMemberCount;
    RtmChannel rtmChannel;
    List<RaisedHandsMember> raisedHandsMemberList=new ArrayList<>();

    public ChannelVm(@NonNull Application application) {
        super(application);
    }

    public void getChannelmemberList(){
        rtmChannel=((AGApplication)getApplication()).getAgoraThread().getmRtmChannel();
        rtmChannel.getMembers(new ResultCallback<List<RtmChannelMember>>() {
            @Override
            public void onSuccess(List<RtmChannelMember> rtmChannelMembers) {
                usersLiveData.postValue(rtmChannelMembers);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                Log.d("fredbeop","获取成员失败"+errorInfo.getErrorCode());
            }
        });
    }

    public void addRaisedHandsMember(RtmMessage rtmMessage,RtmChannelMember rtmChannelMember){
        RaisedHandsMember raisedHandsMember=new RaisedHandsMember(rtmMessage.getText(),rtmChannelMember.getUserId(),false);
        raisedHandsMemberList.add(raisedHandsMember);
        raisedHandsList.postValue(raisedHandsMemberList);
    }

    public LiveData<List<RtmChannelMember>> getUsersLiveData() {
        return usersLiveData;
    }

    public LiveData<List<RaisedHandsMember>> getRaisedHandsMember(){
        return raisedHandsList;
    }


    public void sendPeerMessage(String content,String duser){
        ((AGApplication)getApplication()).getAgoraThread().sendPeerMessage(content,duser);
    }

    public RtcEngine getRtcEngine() {
       return ((AGApplication) getApplication()).getAgoraThread().getRtcEngine();
    }
}
