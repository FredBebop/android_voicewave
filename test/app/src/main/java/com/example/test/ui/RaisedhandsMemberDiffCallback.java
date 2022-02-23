package com.example.test.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.test.vm.RaisedHandsMember;

public class RaisedhandsMemberDiffCallback extends DiffUtil.ItemCallback<RaisedHandsMember> {
    @Override
    public boolean areItemsTheSame(@NonNull RaisedHandsMember oldItem, @NonNull RaisedHandsMember newItem) {
        return oldItem.getUserId().equals(newItem.getUserId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull RaisedHandsMember oldItem, @NonNull RaisedHandsMember newItem) {
        return (oldItem.isInvited()==newItem.isInvited())&&
                (oldItem.getUserId().equals(newItem.getUserId()))&&
                (oldItem.getNickName().equals(newItem.getNickName()));
    }
}
