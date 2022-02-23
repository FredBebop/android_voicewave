package com.example.test.rtm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.agora.rtm.RtmMessage;

public class RtmMessagePool {
    private Map<String, List<RtmMessage>> mOfflineMessageMap = new HashMap<>();

    public void insertOfflineMessage(RtmMessage rtmMessage,String peerID){
        boolean flag=mOfflineMessageMap.containsKey(peerID);
        List<RtmMessage> list=flag?mOfflineMessageMap.get(peerID):new ArrayList<>();

        if(list!=null){
            list.add(rtmMessage);
        }

        if (!flag) {
            mOfflineMessageMap.put(peerID, list);
        }
    }

    public List<RtmMessage> getAllOfflineMessages(String peerID){
        return mOfflineMessageMap.containsKey(peerID)?mOfflineMessageMap.get(peerID):new ArrayList<>();
    }

    public void removeAllOfflineMessages(String peerID){
        mOfflineMessageMap.remove(peerID);
    }
}
