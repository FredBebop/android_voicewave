package com.example.test.ui;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.test.R;
import com.example.test.vm.ChannelListVm;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.v3.MessageDialog;
import com.lxj.xpopup.core.BottomPopupView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CreateRoomPopup extends BottomPopupView {

    private EditText editText;

    private ChannelListVm channelListVm;

    private View layout;

    private Socket socket;

    private OutputStream outputStream;

    private BufferedReader br;

    private String response;

    private Handler handler;

    private String[] tipArray = new String[]{getContext().getString(R.string.public_channel), getContext().getString(R.string.privice_channel)};

    public CreateRoomPopup(Context context, ChannelListVm channelListVm) {
        super(context);
        this.channelListVm = channelListVm;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        layout = getPopupImplView();
        View fl_open = layout.findViewById(R.id.fl_open);
        View fl_password = layout.findViewById(R.id.fl_password);
        TextView tv_add_topic = layout.findViewById(R.id.tv_add_topic);
        TextView tv_tip = layout.findViewById(R.id.tv_tip);
        EditText et_password = layout.findViewById(R.id.et_password);
        Button btn_create = layout.findViewById(R.id.btn_create);

        channelListVm.setCurChannelType(0);
        fl_open.setSelected(true);
        tv_tip.setText(tipArray[0]);

        fl_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_open.setSelected(true);
                fl_password.setSelected(false);
                et_password.setVisibility(INVISIBLE);
                channelListVm.setCurChannelType(0);
                if (channelListVm.getCurTopic().isEmpty()) {
                    tv_tip.setText(tipArray[0]);
                } else {
                    tv_tip.setText(tipArray[0] + ": " + "\"" + channelListVm.getCurTopic() + "\"");
                }
            }
        });
        fl_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_open.setSelected(false);
                fl_password.setSelected(true);
                et_password.setVisibility(VISIBLE);
                channelListVm.setCurChannelType(1);
                if (channelListVm.getCurTopic().isEmpty()) {
                    tv_tip.setText(tipArray[0]);
                } else {
                    tv_tip.setText(tipArray[0] + ": " + "\"" + channelListVm.getCurTopic() + "\"");
                }
            }
        });
        tv_add_topic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetTopicDialog(channelListVm.getCurTopic());
            }
        });
        btn_create.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelListVm.getCurTopic().isEmpty()) {
                    Toast.makeText(getContext(), "取个题目吧", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            try {
                                socket = new Socket("42.192.55.234", 8888);
                                // 判断客户端和服务器是否连接成功
                                Log.d("fredbebop", "连接是否成功：" + socket.isConnected());

                                if (socket.isConnected()) {
                                    Message message = Message.obtain();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                }

                                // 步骤1：从Socket 获得输出流对象OutputStream
                                // 该对象作用：发送数据
                                outputStream = socket.getOutputStream();

                                // 步骤2：写入需要发送的数据到输出流对象中
                                outputStream.write((channelListVm.getGBKTopic() + "\n").getBytes(StandardCharsets.UTF_8));
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
                                ChannelRoom.startActivity(getContext(), channelListVm.getCurTopic(), false, response);
                            } else if (msg.what == 1) {
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
            }
        });
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        channelListVm.setCurTopic("");
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_create_room;
    }

    private void showSetTopicDialog(String tp) {
        MessageDialog.show((AppCompatActivity) getContext(), getContext().getString(R.string.add_topic), getContext().getString(R.string.eg))
                .setCancelable(true)
                .setCustomView(R.layout.layout_topic, new MessageDialog.OnBindView() {
                    @Override
                    public void onBind(MessageDialog dialog, View v) {
                        TextView tip = v.findViewById(R.id.tv_tip);
                        editText = v.findViewById(R.id.et_topic);
                        if (!(tp.isEmpty())) {
                            editText.setText(tp);
                            editText.setSelection(tp.length());
                            tip.setText("还能输入" + (60 - tp.length()) + "个字符");
                        }
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                tip.setText("还能输入" + (60 - s.length()) + "个字符");
                            }
                        });
                    }
                }).setOkButton(getContext().getString(R.string.setting))
                .setCancelButton(getContext().getString(R.string.cancel), new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        baseDialog.doDismiss();
                        return true;
                    }
                }).setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                TextView textView = layout.findViewById(R.id.tv_tip);
                if (editText.getText().toString().trim().isEmpty()) {
                    channelListVm.setCurTopic("");
                    channelListVm.setGBKTopic("");
                    textView.setText(tipArray[channelListVm.getCurChannelType()]);
                } else {
                    channelListVm.setCurTopic(editText.getText().toString());
                    try {
                        channelListVm.setGBKTopic(URLEncoder.encode(editText.getText().toString(), "GBK"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    textView.setText(tipArray[channelListVm.getCurChannelType()] + ": " + "\"" + channelListVm.getCurTopic() + "\"");
                }
                baseDialog.doDismiss();
                return true;
            }
        });

    }
}
