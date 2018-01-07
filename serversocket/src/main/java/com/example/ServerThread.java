package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;




/**
 * Created by Administrator on 2018/1/7/007.
 */

public class ServerThread implements Runnable {
    private Socket clientSocket;

    //socket所对应的输入流
    InputStream br = null;
    //目标socket所对应的输出流
    OutputStream outputStream = null;


    public ServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            br = clientSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {


        try {
            //读取从客户端发送过来的数据
            InputStream inputStream = clientSocket.getInputStream();
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                String data = new String(buffer, 0, len);
                System.out.println(data);
                //判断是否是注册id 并将id 和socket存入Map
                if (data.startsWith("#")) {
                    MyServer.socketMap.put(data, clientSocket);
                } else {
                    //将数据发送给指定的客户端
                    String[] split = data.split("#");
                    System.out.println("#" + split[0]);
                    //根据id获取指定的socket
                    Socket c = MyServer.socketMap.get("#" + split[0]);
                    //获取socket的输出流 并将消息写入
                    outputStream = c.getOutputStream();
                    outputStream.write(split[1].getBytes());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
