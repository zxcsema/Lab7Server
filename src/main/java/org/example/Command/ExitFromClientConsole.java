package org.example.Command;

import com.google.inject.Inject;
import org.example.Collection.Collection;


import javax.inject.Named;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ExitFromClientConsole extends AbstractCommand implements Command {

    public ExitFromClientConsole(){
        super("ExitFromClientConsole", 0);


    }

    @Override
    public void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException {
        Collection collection = Collection.getInstance();
        collection.clear();
        Files.delete(Path.of("worker.csv"));
    }


    @Override
    public String getDescription() {
        return "exit : завершить программу (без сохранения в файл)";
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public String getMessageToResponse() {
        return "SERVER_SHUTDOWN";
    }
}
