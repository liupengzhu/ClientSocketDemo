package com.example.clientsocketdemo;



import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    private Handler handler;

    EditText regis_text;
    EditText target_text;
    EditText content_text;
    Button regis_button;
    Button send_button;
    Button unConnect_button;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化view
        initView();
        //主线程handler 将获取到的服务器的信息展示在textview上
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == 0x123){
                    Log.d("main",msg.obj.toString());
                    textView.append(msg.obj.toString());
                }
            }
        };
        //创建客服端子线程并开启子线程
        final ClientThread c = new ClientThread(handler);
        new Thread(c).start();
        //注册id
        regis_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regis_text.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"注册信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    String regisContent = "#" + regis_text.getText().toString().trim();
                    Message message = new Message();
                    message.what = 0x345;
                    message.obj = regisContent;
                    c.revHandler.sendMessage(message);

                    regis_text.setText("");
                }

            }
        });
        //发送消息 必须指定接收方的id
        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(target_text.getText().toString().trim().isEmpty()||content_text.getText().toString().trim().isEmpty()){
                    Toast.makeText(MainActivity.this,"信息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    String content = target_text.getText().toString().trim()+"#" + content_text.getText().toString().trim();
                    Message message = new Message();
                    message.what = 0x345;
                    message.obj = content;
                    c.revHandler.sendMessage(message);

                    target_text.setText("");
                    content_text.setText("");
                }
            }
        });

        unConnect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initView() {
        regis_text = (EditText) findViewById(R.id.regis);
        target_text = (EditText) findViewById(R.id.target);
        content_text = (EditText) findViewById(R.id.content);
        regis_button = (Button) findViewById(R.id.regis_button);
        send_button = (Button) findViewById(R.id.send_button);
        unConnect_button = (Button) findViewById(R.id.unconnect_button);
        textView = (TextView) findViewById(R.id.content_text);
    }
}
