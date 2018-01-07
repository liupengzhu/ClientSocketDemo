package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/7/007.
 */

public class MyServer {
    private static int port = 30000;
    public static Map<String,Socket> socketMap = new LinkedHashMap<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        while (true){
            System.out.println("服务器已启动,等待客户端的链接。。。");
            Socket socket = serverSocket.accept();
            System.out.println("客服端已经链接。。。");
            new Thread(new ServerThread(socket)).start();


        }


    }


}
