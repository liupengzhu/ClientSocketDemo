package com.example.clientsocketdemo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Administrator on 2018/1/7/007.
 */

public class ClientThread implements Runnable {
    //向UI线程发射消息的handler
    private Handler handler;

    //socket所对应的输入输出流
    InputStream reader = null;
    OutputStream writer = null;
    private Socket s;

    //接受Ui线程消息的Handler；
    public  Handler revHandler;

    public ClientThread() {
        super();
    }

    public ClientThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

        try {
            s = new Socket("192.168.0.103",30000);
            reader = s.getInputStream();
            writer = s.getOutputStream();

            //启动一条子线程来读取服务器响应的数据
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String content = null;
                    try {
                        byte[] buffer = new byte[1024];
                        int len = -1;
                        while ((len = reader.read(buffer)) != -1) {
                            String data = new String(buffer, 0, len);
                            //每当读取到来自服务器的消息之后，发送消息通知程序界面显示数据
                            Message message = new Message();
                            message.what = 0x123;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            //为当前线程初始化Looper
            Looper.prepare();
            revHandler= new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == 0x345){
                        try {
                           //将获取的信息发送给服务器
                            writer.write(msg.obj.toString().getBytes());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();





        }catch (SocketTimeoutException e1){
            System.out.printf("网络连接超时");
        }
        catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
