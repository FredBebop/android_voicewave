package com.example.test;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.network.MysqlService;

import java.util.List;

import io.agora.rtm.RtmChannelMember;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    List<RtmChannelMember> userList;
    Context context;
    MysqlService mysqlService;

    public UserAdapter(Context context, MysqlService mysqlService, List<RtmChannelMember> userList) {
        this.context = context;
        this.userList = userList;
        this.mysqlService = mysqlService;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        SquircleImageView user_icon;

        public ViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.tv_name);
            user_icon = view.findViewById(R.id.iv_user);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context)
                .inflate(R.layout.item_listener, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RtmChannelMember broadcaster =userList.get(position);
        holder.user_name.setText(broadcaster.getUserId());
        holder.user_icon.setImageResource(R.drawable.d);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

}
