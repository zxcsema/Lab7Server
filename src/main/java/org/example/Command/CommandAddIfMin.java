package org.example.Command;

import org.example.ClientAction.*;
import org.example.ClientAction.ClientSession;
import org.example.Collection.Collection;
import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;
import org.jooq.DSLContext;


import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.*;

public class CommandAddIfMin extends AbstractCommand implements Command {
    private Worker newWorker;
    String result;
    private final DSLContext connection = ConnectionDB.getConnection();
    private final WorkerRepository workerRepository;

    @Inject
    public CommandAddIfMin(  @Named("DAO") WorkerRepository workerRepository) throws SQLException {
        super("add_if_min", 0);

        this.workerRepository = workerRepository;
    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException {

        if (Mode.SCRIPT.equals(mode)) {
            Collection collection = Collection.getInstance();
            if (newWorker.isNull()) {
                return;
            }
            if (Collection.getInstance().isEmpty()) {
                collection.add(newWorker);
                result = "Элемент добавлен в пустую коллекцию.";
                return;
            }

            Worker minWorker = collection.getFirst();
            for (Worker worker : Collection.getInstance().getAll()) {
                if (worker.compareTo(minWorker) < 0) {
                    minWorker = worker;
                }
            }

            if (newWorker.compareTo(minWorker) < 0) {
                collection.add(newWorker);
                result = "Элемент добавлен, так как он минимальный.";
            } else {
                result = "Элемент не добавлен, так как он не минимальный.";
            }
        } else {
            Collection collection = Collection.getInstance();
            ClientSession session = ClientConnection.getCurrentSession(client);
            if (!session.isWaitingForWorker()) {
                session.setWaitingForWorker(true);
                result = "interactive";

            } else {
                if (collection.isEmpty()) {
                    workerRepository.save(newWorker, client);
                    collection.add(newWorker);
                    result = "Элемент добавлен в пустую коллекцию.";

                    session.setWaitingForWorker(false);

                    return;
                }

                Worker minWorker = collection.getFirst();
                for (Worker worker : collection.getAll()) {
                    if (worker.compareTo(minWorker) < 0) {
                        minWorker = worker;
                    }
                }

                if (newWorker.compareTo(minWorker) < 0) {
                    workerRepository.save(newWorker, client);
                    collection.add(newWorker);
                    result = "Элемент добавлен, так как он минимальный.";
                } else {
                    result = "Элемент не добавлен, так как он не минимальный.";
                }

                    session.setWaitingForWorker(false);


            }
        }
    }




    @Override
    public String getDescription() {
        return "add_if_min : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции";
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
        return result;
    }

    public void setNewWorker(Worker newWorker){
        this.newWorker = newWorker;
    }

}
