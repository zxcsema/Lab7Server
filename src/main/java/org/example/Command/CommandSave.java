package org.example.Command;

import org.example.ClientAction.ClientConnection;
import org.example.DateBase.WorkerRepository;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.nio.channels.SocketChannel;


public class CommandSave extends AbstractCommand implements Command {
    private final WorkerRepository workerRepository;
    @Inject
    public CommandSave(@Named("DAO") WorkerRepository workerRepository) {

        super("save", 0);

        this.workerRepository = workerRepository;
    }

    @Override
    public void execute(SocketChannel client, Mode mode) throws IOException {
        workerRepository.deleteWorkersByLoginAndSaveFalse(ClientConnection.getNameClient(client));
    }



    @Override
    public String getDescription() {
        return "save : сохранить коллекцию в файл";
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public String getMessageToResponse() {
        return "";
    }


}