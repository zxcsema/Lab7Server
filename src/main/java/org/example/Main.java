package org.example;
import org.example.Config.ServerConfig;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.getServer().startServer();
    }
}
