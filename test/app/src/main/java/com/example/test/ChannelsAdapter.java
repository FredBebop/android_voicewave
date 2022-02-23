package com.example.test;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureSession;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.ui.ChannelRoom;
import com.example.test.network.Channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsAdapter.ViewHolder> {
    Context context;
    List<Channels.Channel> channels;

    private Socket socket;

    private OutputStream outputStream;

    private BufferedReader br;

    private String response;

    private Handler handler;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView user_count;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_channel_name);
            user_count = view.findViewById(R.id.tv_user_num);
        }
    }

    public ChannelsAdapter(Context context, List<Channels.Channel> channels) {
        this.channels = channels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Channels.Channel channel = channels.get(position);
        holder.user_count.setText(String.valueOf(channel.getUser_count()));
        try {
            holder.title.setText(URLDecoder.decode(channel.getChannel_name(),"GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //            TODO:点击范围不对
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Channels.Channel channel = channels.get(position);

                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        try {
                            socket = new Socket("42.192.55.234", 8888);
                            // 判断客户端和服务器是否连接成功
                            Log.d("fredbebop", "连接是否成功：" + socket.isConnected());
                            if(socket.isConnected()){
                                Message message=Message.obtain();
                                message.what=1;
                                handler.sendMessage(message);
                            }

                            // 步骤1：从Socket 获得输出流对象OutputStream
                            // 该对象作用：发送数据
                            outputStream = socket.getOutputStream();

                            // 步骤2：写入需要发送的数据到输出流对象中
                            outputStream.write(( channel.getChannel_name()+ "\n").getBytes(StandardCharsets.UTF_8));
                            // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                            // 步骤3：发送数据到服务端
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();



                handler = new Handler(Looper.myLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        if (msg.what == 0) {
                            try {
                                ChannelRoom.startActivity(v.getContext(), URLDecoder.decode(channel.getChannel_name(),"GBK"), true, response);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            Log.d("fredbebop",channel.getChannel_name());
                        }else if (msg.what==1){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                                        // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                                        response = br.readLine();

                                        // 步骤4:通知主线程,将接收的消息显示到界面
                                        Message msg = Message.obtain();
                                        msg.what = 0;
                                        handler.sendMessage(msg);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                };
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

}
