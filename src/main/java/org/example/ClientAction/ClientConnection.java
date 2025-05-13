package org.example.ClientAction;

import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnection {
    private static boolean hasDiscard  =false;
    private static SocketChannel socketChannel;
    private static final Map<SocketChannel, ClientSession> sessions = new ConcurrentHashMap<>();
    private static final Map<SocketChannel, String> sessionsName = new ConcurrentHashMap<>();
    private static final Map<SocketChannel, Boolean> sessionsHasDiscrad = new ConcurrentHashMap<>();
    private static final Map<SocketChannel, Boolean> sessionsMode = new ConcurrentHashMap<>();


    public static void set(SocketChannel socket) {
        socketChannel = socket;
    }

    public static SocketChannel get() {
        return socketChannel;
    }

    public static boolean getHasDiscard(){

        return hasDiscard;
    }


    public static void setHasDiscard(boolean bool){

        hasDiscard = bool;
    }

    public static ClientSession getCurrentSession(SocketChannel currentSocketChannel) {
        return sessions.get(currentSocketChannel);
    }

    public static String getNameClient(SocketChannel socketChannel) {
        return sessionsName.get(socketChannel);
    }

    public static void setMode(SocketChannel socketChannel, boolean script) {
        sessionsMode.put(socketChannel, script);
    }

    public static boolean getMode(SocketChannel socketChannel) {
        return sessionsMode.get(socketChannel);
    }

    public static void registerClient(SocketChannel socketChannel) {
        sessionsMode.put(socketChannel, false);
        sessions.put(socketChannel, new ClientSession());
    }

    public static void registerNameClient(SocketChannel socketChannel, String login) {
        sessionsName.put(socketChannel, login);
    }



    public static void registerState(SocketChannel socketChannel, Boolean script) {
        sessionsMode.put(socketChannel, script);
    }

    public static boolean getDiscardClient(SocketChannel socketChannel) {
        return sessionsHasDiscrad.get(socketChannel);

    }

    public static void registerDiscardClient(SocketChannel socketChannel) {
        sessionsHasDiscrad.put(socketChannel, true);
    }

}
