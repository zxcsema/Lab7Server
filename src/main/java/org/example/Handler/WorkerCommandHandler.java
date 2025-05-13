package org.example.Handler;

import org.example.ClientAction.ClientConnection;
import org.example.ClientAction.ClientSession;
import org.example.Command.CommandAddElement;
import org.example.Command.CommandAddIfMin;
import org.example.Command.CommandUpdateId;
import org.example.Entites.Worker;
import org.example.Command.*;
import org.example.Manager.CommandManager;
import org.example.Response.Message;
import org.example.Send.Sender;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class WorkerCommandHandler {
    private final CommandAddElement commandAddElement;
    private final CommandAddIfMin commandAddIfMin;
    private final CommandUpdateId commandUpdateId;
    private final Sender sendSerializedObject;
    private final Map<String, BiConsumer<Worker, SocketChannel>> commandMap = new HashMap<>();

    public WorkerCommandHandler(CommandAddElement commandAddElement,
                                CommandAddIfMin commandAddIfMin,
                                CommandUpdateId commandUpdateId,
                                Sender sendSerializedObject) {
        this.commandAddElement = commandAddElement;
        this.commandAddIfMin = commandAddIfMin;
        this.commandUpdateId = commandUpdateId;
        this.sendSerializedObject = sendSerializedObject;
    }

    public void handleCommand(String commandName, Worker worker, SocketChannel client) {

        commandMap.put("add", this::executeAdd);
        commandMap.put("add_if_min", this::executeAddIfMin);
        commandMap.put("update_id", this::executeUpdateId);


        BiConsumer<Worker, SocketChannel> command = commandMap.get(ClientConnection.getCurrentSession(client).getLastCommandName());
        if (command != null) {
            command.accept(worker, client);
        }
    }


    private  void executeAdd(Worker worker, SocketChannel client){
            commandAddElement.setNewWorker(worker);
            try {
                commandAddElement.execute(client, Mode.NO_SCRIPT);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                sendSerializedObjectAsync(client, new Message(commandAddElement.getMessageToResponse()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    private void executeAddIfMin(Worker worker, SocketChannel client) {
        commandAddIfMin.setNewWorker(worker);
        try {
            commandAddIfMin.execute(client, Mode.NO_SCRIPT);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            sendSerializedObjectAsync(client, new Message(commandAddIfMin.getMessageToResponse()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeUpdateId(Worker worker, SocketChannel client)  {
        commandUpdateId.setNewWorker(worker);
        try {
            commandUpdateId.execute(client,Mode.NO_SCRIPT);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            sendSerializedObjectAsync(client, new Message(commandUpdateId.getMessageToResponse()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void sendSerializedObjectAsync(SocketChannel client, Object obj) throws IOException {
        sendSerializedObject.sendSerializedObject(client, obj);
    }

}
