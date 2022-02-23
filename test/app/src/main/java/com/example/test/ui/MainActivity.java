package com.example.test.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.vm.MainVm;
import com.lxj.xpopup.XPopup;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    TextView register;
    Button login;
    CheckBox rememberMe;
    MainVm mainVm;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.et_name);
        password = findViewById(R.id.et_password);
        login = findViewById(R.id.btn_login);
        register=findViewById(R.id.tx_register);
        rememberMe=findViewById(R.id.remember_me);
        mainVm =new ViewModelProvider(this).get(MainVm.class);
        if(mainVm.isRemember()){
            username.setText(mainVm.getUsername());
            password.setText(mainVm.getpassword());
            rememberMe.setChecked(true);
        }
        login(login);
        register(register);

    }


    public void login(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        switch (mainVm.rl(username.getText().toString(), password.getText().toString())) {
                            case 1: {
                                mainVm.rememberMe(rememberMe.isChecked(), username.getText().toString(), password.getText().toString());
                                ChannelRoomList.startActivity(MainActivity.this);
                                break;
                            }
                            case 2: {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }

                            case 3: {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this, "注册个账号吧", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }
                        }
                    }
                }).start();

                view.setSelected(!view.isSelected());
            }
        });
    }

    public void register(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(MainActivity.this)
                        .enableDrag(true)
                        .isDestroyOnDismiss(true)
                        .asCustom(new RegisterPopup(MainActivity.this, mainVm))
                        .show();
            }
        });

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}