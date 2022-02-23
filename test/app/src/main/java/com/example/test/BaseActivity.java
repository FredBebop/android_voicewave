package com.example.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.test.rtm.RtmChatManager;
import com.gyf.immersionbar.ImmersionBar;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import io.agora.rtc.RtcEngine;

public abstract class BaseActivity extends AppCompatActivity {
    private final int BASE_VALUE_PERMISSION = 0X0001;
    private final int PERMISSION_REQ_ID_RECORD_AUDIO = BASE_VALUE_PERMISSION + 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.app_color)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.app_color,0.15f)
                .init();
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO, PERMISSION_REQ_ID_RECORD_AUDIO)){
            ((AGApplication) getApplication()).init();
            try {
                initUIandEvent();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    protected abstract void initUIandEvent() throws UnsupportedEncodingException;

    protected abstract void deInitUIandEvent();


    public boolean checkSelfPermission(String permission, int requestCode) {
        Log.d("BaseActivity","checkSelfPermission " + permission + " " + requestCode);
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("BaseActivity","onRequestPermissionsResult " + requestCode + " " + Arrays.toString(permissions) + " " + Arrays.toString(grantResults));
        switch(requestCode){
            case PERMISSION_REQ_ID_RECORD_AUDIO:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((AGApplication) getApplication()).init();
                    try {
                        initUIandEvent();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else {
                    finish();
                }
                break;
            }
        }
    }

    protected RtcEngine getRtcEngine() {
        return ((AGApplication) getApplication()).getAgoraThread().getRtcEngine();
    }

    protected final AgoraThread getAgoraThread() {
        return ((AGApplication) getApplication()).getAgoraThread();
    }

    protected final EngineEventHandler getEvent() {
        return ((AGApplication) getApplication()).getAgoraThread().getEventHandler();
    }

    protected final RtmChatManager getRtmChatManager(){
        return ((AGApplication)getApplication()).getAgoraThread().getRtmChatManager();
    }
    protected final String getMyNickName(){
        return ((AGApplication)getApplication()).getMyProfile().getMyNickName();
    }
}
