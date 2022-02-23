package com.example.test.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.test.R;
import com.example.test.vm.ChannelVm;
import com.gyf.immersionbar.ImmersionBar;

import io.agora.rtc.Constants;

public class InviteDialogFragment extends DialogFragment {
    private ChannelVm channelVm;
    private String peerUser;
    private OnDissmissCallback onDissmissCallback=null;
    private View view;

    public InviteDialogFragment(ChannelVm channelVm,String peeruser){
        this.channelVm=channelVm;
        this.peerUser=peeruser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.dialog_invite,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImmersionBar.with(this)
                .fitsSystemWindows(true)
                .statusBarColor(R.color.green_color)
                .init();
        Button btnJoin =view.findViewById(R.id.btn_join);
        Button btnLater=view.findViewById(R.id.btn_later);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                channelVm.sendPeerMessage("accept",peerUser);
                channelVm.getRtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
                dismiss();
            }
        });
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                channelVm.sendPeerMessage("refuse",peerUser);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.MyDialog);
        setCancelable(false);
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

   public void show(FragmentManager manager,OnDissmissCallback dissmissCallback){
        show(manager,"invite");
        this.onDissmissCallback=dissmissCallback;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onDissmissCallback.dismiss();
    }

    interface OnDissmissCallback{
        void dismiss();
    }
}
