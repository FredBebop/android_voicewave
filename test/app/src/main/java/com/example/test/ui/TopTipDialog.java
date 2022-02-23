package com.example.test.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.test.R;
import com.gyf.immersionbar.ImmersionBar;

public class TopTipDialog extends DialogFragment {
    private String tip;
    private int bgColor = R.color.green_color;
    private boolean cancelable = true;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_top_tip, container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(cancelable);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(bgColor)
                .init();

       LinearLayout linearLayout= view.findViewById(R.id.ll_bg);
        TextView textView=view.findViewById(R.id.tv_tip);
        linearLayout.setBackgroundColor(getResources().getColor(bgColor,null));
        textView.setText(tip);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.MyDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window=getDialog().getWindow();
        window.setGravity(Gravity.TOP);
        window.setWindowAnimations(R.style.TopAnimation);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view=null;
    }

    public  void showDialog(FragmentManager manager,String tip){
        this.tip=tip;
        show(manager,"tip");
        if(cancelable){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
