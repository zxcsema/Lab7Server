package org.example.Command;

import com.google.inject.Inject;

import org.example.ClientAction.*;
import org.example.ClientAction.ClientSession;
import org.example.Collection.Collection;
import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;
import org.jooq.DSLContext;

import java.nio.channels.SocketChannel;
import java.sql.*;


import javax.inject.Named;
import java.io.*;

public class CommandAddElement extends AbstractCommand implements Command {
    private String file;
    private int line;
    private Worker newWorker;
    String result;
    private final DSLContext connection = ConnectionDB.getConnection();
private final WorkerRepository workerRepository;

    @Inject
    public CommandAddElement(@Named("DAO") WorkerRepository workerRepository) throws SQLException {
        super("add", 0);
        this.workerRepository = workerRepository;
    }

    @Override
    public  void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException {
        Worker newWorker;

        if (Mode.SCRIPT.equals(mode)) {

        } else {
            ClientSession session = ClientConnection.getCurrentSession(client);
            if (!session.isWaitingForWorker()) {
                session.setWaitingForWorker(true);
                result = "interactive";

            } else {
                this.newWorker.setCreator(ClientConnection.getNameClient(client));
                workerRepository.save(this.newWorker, client);
                Collection collection = null;
                try {
                    collection = Collection.getInstance();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                collection.add(this.newWorker);
                result = "Элемент добавлен";

                session.setWaitingForWorker(false);


            }
        }

    }


    @Override
    public String getDescription() {
        return "add : добавить новый элемент в коллекцию";
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public void setScriptContext(String file, int line) {
        this.file = file;
        this.line = line;
    }

    public int getLinesToSkipInScript() {
        return 10; // Количество строк, которые нужно пропустить
    }

    @Override
    public String getMessageToResponse() {
        return result ;
    }


    public void setNewWorker(Worker newWorker){
        this.newWorker = newWorker;
    }

}