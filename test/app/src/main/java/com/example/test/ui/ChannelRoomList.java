package com.example.test.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.AGApplication;
import com.example.test.ChannelsAdapter;
import com.example.test.R;
import com.example.test.network.Channels;
import com.example.test.network.Response;
import com.example.test.vm.ChannelListVm;
import com.google.android.material.navigation.NavigationView;
import com.lxj.xpopup.XPopup;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChannelRoomList extends AppCompatActivity {
    ChannelListVm channelListVm;
    List<Channels.Channel> channelList = new ArrayList<>();
    ChannelsAdapter channelsAdapter;
    RecyclerView rvChannels;
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channellist);
        rvChannels = findViewById(R.id.rv_channel);
        //初始化身份
        ((AGApplication)getApplication()).getMyProfile().setBroadcaster(false);

        drawerLayout=findViewById(R.id.dl_channel);

        TextView nickName=findViewById(R.id.tv_user_name);
        nickName.setText(((AGApplication)getApplication()).getMyProfile().getMyNickName());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_menu);
        View view=navigationView.getHeaderView(0);
        TextView slidingNickName=view.findViewById(R.id.tv_nickname);
        slidingNickName.setText(((AGApplication)getApplication()).getMyProfile().getMyNickName());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_bug_report:{
                        Uri uri = Uri.parse("mailto:fred612330@icloud.com");
                        String[] email = {"fred612330@icloud.com"};
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "bug提交"); // 主题
                        intent.putExtra(Intent.EXTRA_TEXT, "欢迎您的建议"); // 正文
                        startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
                        break;
                    }
                    case R.id.nav_message:{
                        Toast.makeText(getApplicationContext(),"待开发",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_share:{
                        Toast.makeText(getApplicationContext(),"待开发",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_profile:{
                        Toast.makeText(getApplicationContext(),"待开发",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return true;
            }
        });

        Button button = findViewById(R.id.btn_create);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(ChannelRoomList.this)
                        .enableDrag(true)
                        .isDestroyOnDismiss(true)
                        .asCustom(new CreateRoomPopup(ChannelRoomList.this, channelListVm))
                        .show();

                ((AGApplication)getApplication()).getMyProfile().setBroadcaster(true);
            }
        });

        channelListVm = new ViewModelProvider(this).get(ChannelListVm.class);
        channelListVm.init();
        channelListVm.getChannelsLiveData().observe(this, new Observer<Response<Channels>>() {
            @Override
            public void onChanged(Response<Channels> channelsResponse) {
                List<Channels.Channel> channels = Arrays.asList(channelsResponse.getData().getChannels());
                channelList.clear();
                channelList.addAll(channels);
                channelsAdapter.notifyDataSetChanged();
            }
        });
        setupRecyclerView();
        refresh();


    }

    private void setupRecyclerView() {
        if (channelsAdapter == null) {
            channelsAdapter = new ChannelsAdapter(ChannelRoomList.this, channelList);
            rvChannels.setLayoutManager(new LinearLayoutManager(this));
            rvChannels.setAdapter(channelsAdapter);
            rvChannels.setItemAnimator(new DefaultItemAnimator());
            rvChannels.setNestedScrollingEnabled(true);
        } else {
            channelsAdapter.notifyDataSetChanged();
        }
    }

    private void refresh() {
        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                channelListVm.refreshChannelsLiveData();
                channelListVm.getChannelsLiveData().observe(ChannelRoomList.this, new Observer<Response<Channels>>() {
                    @Override
                    public void onChanged(Response<Channels> channelsResponse) {
                        List<Channels.Channel> channels = Arrays.asList(channelsResponse.getData().getChannels());
                        channelList.clear();
                        channelList.addAll(channels);
                        channelsAdapter.notifyDataSetChanged();
                    }
                });
                refreshLayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
            }
        });
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ChannelRoomList.class);
        context.startActivity(intent);
    }

    public void openNavigation(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }
}
