package org.example;
import org.example.Config.ServerConfig;


public class Main {
    public static void main(String[] args) {
        try{
            ServerConfig serverConfig = new ServerConfig();
            serverConfig.getServer().startServer();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

