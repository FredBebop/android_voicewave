package com.example.test.ui;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.test.R;
import com.example.test.vm.MainVm;
import com.lxj.xpopup.core.BottomPopupView;

public class RegisterPopup extends BottomPopupView {

    private View layout;
    private TextView username;
    private TextView password;
    private Button register;
    private MainVm mainVm;

    public RegisterPopup(@NonNull Context context, MainVm mainVm) {
        super(context);
        this.mainVm=mainVm;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        layout = getPopupImplView();
        username=layout.findViewById(R.id.et_name);
        password=layout.findViewById(R.id.et_password);
        register=layout.findViewById(R.id.btn_register);

        register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mainVm.register(username.getText().toString(),password.getText().toString());
                    }
                }).start();
                dismiss();
            }
        });

    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_register ;
    }
}
