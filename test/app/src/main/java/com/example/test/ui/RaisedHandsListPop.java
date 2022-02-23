package com.example.test.ui;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.test.R;
import com.example.test.RaisedHandsListAdapter;
import com.example.test.vm.ChannelVm;
import com.example.test.vm.RaisedHandsMember;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.List;

public class RaisedHandsListPop extends BottomPopupView implements OnItemChildClickListener {

    private Context mContext;
    private ChannelVm channelVm;
    private RecyclerView rvList;
    private RaisedHandsListAdapter raisedHandsListAdapter;


    public RaisedHandsListPop(@NonNull Context context,ChannelVm channelVm) {
        super(context);
        this.mContext=context;
        this.channelVm=channelVm;

    }

    @Override
    public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
        if(!raisedHandsListAdapter.getItem(position).isInvited()){
            raisedHandsListAdapter.getItem(position).setInvited(true);
            raisedHandsListAdapter.notifyItemChanged(position);
            channelVm.sendPeerMessage("ok",raisedHandsListAdapter.getItem(position).getNickName());
        }

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_raised_hands_list;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        rvList=findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        raisedHandsListAdapter=new RaisedHandsListAdapter();
        raisedHandsListAdapter.setDiffCallback(new RaisedhandsMemberDiffCallback());
        rvList.setAdapter(raisedHandsListAdapter);
        raisedHandsListAdapter.setEmptyView(R.layout.layout_empty_raised_hands);
        raisedHandsListAdapter.addChildClickViewIds(R.id.btn_invite);
        raisedHandsListAdapter.setOnItemChildClickListener(this::onItemChildClick);

        channelVm.getRaisedHandsMember().observe((ChannelRoom) this.mContext, new Observer<List<RaisedHandsMember>>() {
            @Override
            public void onChanged(List<RaisedHandsMember> raisedHandsMembers) {
                raisedHandsListAdapter.setDiffNewData(raisedHandsMembers);
            }
        });
    }
}
