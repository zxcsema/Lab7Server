package org.example.Executor;

import org.example.Command.Command;
import org.example.Command.Mode;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class CommandExecutor {
    public void executeCommandNoScript(SocketChannel client, Command command) throws IOException, ClassNotFoundException, SQLException {
        command.execute(client, Mode.NO_SCRIPT);
    }
}
