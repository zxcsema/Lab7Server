package org.example.ClientAction;

import org.example.Command.SreialazeCommand;

import java.nio.channels.SocketChannel;

public class ClientSession {
    private boolean waitingForWorker = false;
private  String lastCommandName1 = "";
    private boolean script;

    public boolean isWaitingForWorker() {
        return waitingForWorker;
    }

    public void setWaitingForWorker(boolean waitingForWorker) {
        this.waitingForWorker = waitingForWorker;
    }
    public  void setState(boolean script){
        this.script = script;
    }
    public boolean getState(boolean script){
        return script;
    }

    public  void setLastCommandName(String lastCommandName){
        lastCommandName1 = lastCommandName;
    }
    public  String getLastCommandName(){
        return lastCommandName1;
    }
}
