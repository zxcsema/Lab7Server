package org.example.Command;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class CommandExit extends AbstractCommand implements Command {
    private final Command commandSave;
    @Inject
    public CommandExit( @Named("save") Command commandSave){
        super("exit", 0);
        this.commandSave = commandSave;
    }

    @Override
    public void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException {

            commandSave.execute(client, Mode.NO_SCRIPT);

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
