package com.example.test;

import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.test.vm.RaisedHandsMember;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RaisedHandsListAdapter extends BaseQuickAdapter<RaisedHandsMember,BaseViewHolder> {

    public RaisedHandsListAdapter(){
        super(R.layout.item_raised_hands);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RaisedHandsMember raisedHandsMember) {
        baseViewHolder.setText(R.id.tv_name,raisedHandsMember.getNickName());
        Button button=baseViewHolder.getView(R.id.btn_invite);
        if(raisedHandsMember.isInvited()){
            button.setText("已邀请");
            baseViewHolder.setEnabled(R.id.btn_invite,false);
        }else{
            button.setText("邀请");
            baseViewHolder.setEnabled(R.id.btn_invite,true);
        }
        button.setSelected(raisedHandsMember.isInvited());
        baseViewHolder.setImageResource(R.id.iv_user,R.drawable.a);
    }

}
