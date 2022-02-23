package com.example.test.vm;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.test.AGApplication;
import com.example.test.R;
import com.example.test.network.MysqlService;
import com.example.test.network.Repository;

public class MainVm extends AndroidViewModel {
    MysqlService mysqlService=((AGApplication)getApplication()).getMysqlService();;
    SharedPreferences.Editor editor;
    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getApplication());

    public MainVm(@NonNull Application application) {
        super(application);
    }

    public int rl(String username,String password){
        int flag;
        if(mysqlService.isUsername(username)){
            if( mysqlService.isPassword(username,password)){
                setProfile(((AGApplication) getApplication()).getMyProfile(),mysqlService.queryUid(username),username);
                flag= 1;
            }else{
                flag= 2;
            }
        }else{
            flag= 3;
        }
        Log.d("fredbebop","flag:"+flag);
        return flag;
    }

    public void register(String username,String password){
        mysqlService.register(username, password);
    }

    public void setProfile(MyProfile myProfile,int muid,String myNickName){
        myProfile.setmUid(muid);
        myProfile.setMyNickName(myNickName);
    }

    public void rememberMe(boolean check,String username, String password){
        editor= PreferenceManager.getDefaultSharedPreferences(getApplication()).edit();
        if(check){
            editor.putBoolean("rememberPw",true);
            editor.putString("username",username);
            editor.putString("password",password);
        }else {
            editor.clear();
        }
        editor.apply();
    }

    public boolean isRemember(){
        return preferences.getBoolean("rememberPw",false);
    }

    public  String getUsername(){
        return  preferences.getString("username","");
    }

    public String getpassword(){
        return preferences.getString("password","");
    }
}
