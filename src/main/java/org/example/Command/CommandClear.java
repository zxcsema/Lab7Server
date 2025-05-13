package org.example.Command;
import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CommandClear extends AbstractCommand implements Command {
    private final DSLContext connection = ConnectionDB.getConnection();
    private final WorkerRepository workerRepository;



    @Inject
    public CommandClear(@Named("DAO") WorkerRepository workerRepository) throws SQLException {

        super("clear", 0);
        this.workerRepository = workerRepository;
    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();
        workerRepository.deleteWorkersByLoginAndSaveFalse(ClientConnection.getNameClient(client));
        collection.clear();
    }







    @Override
    public String getDescription() {
        return "clear : " +
                "очистить коллекцию";
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public String getMessageToResponse() {
        return "коллекция очищена";
    }
}
