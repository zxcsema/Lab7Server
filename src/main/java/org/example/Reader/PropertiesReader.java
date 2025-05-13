package org.example.Reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private static String user;
    private static String dbName;
    private static String port;
    private static String password;
    private static String host;
    private static boolean flag = true;

private static String CONFIG_FILE_BD = "db.properties";
    public static void read() {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_BD)) {
            props.load(input);

            host = props.getProperty("Host");
            port = props.getProperty("Port");
            dbName = props.getProperty("Basename");
            user = props.getProperty("User");
            password = props.getProperty("Password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void init(){
        if(flag){
            read();
            flag = false;
        }
    }

    public static String getUser() {
        init();
        return user;
    }

    public static String getDbName() {
        init();
        return dbName;
    }

    public static String getPort() {
        init();
        return port;
    }

    public static String getPassword() {
        init();
        return password;
    }

    public static String getHost() {
        init();
        return host;
    }
}
