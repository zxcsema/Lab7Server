package org.example.Response;

import org.example.Command.Command;
import org.example.Command.CommandExecuteScript;
import org.example.Command.CommandExit;
import org.example.Executor.CommandExecutor;
import org.example.Manager.CommandManager;
import java.io.*;
import java.nio.channels.*;
import java.sql.SQLException;
import java.util.Map;

public class Response {
    private final CommandExecutor commandExecutor;
    private final CommandManager commandManager;

    public Response() {
        this.commandExecutor = new CommandExecutor();
        this.commandManager = CommandManager.getInstance();
    }

    public  Message getResponse(SocketChannel client, String commandName, String commandArg) throws IOException, ClassNotFoundException {
        client.configureBlocking(false);
        Selector selector = Selector.open();
        client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        Map<String, Command> commandMap = commandManager.getCommands();
        Command command = commandMap.get(commandName);
        System.out.println("from client " + commandName + " " + command.getArgument());

        if (command instanceof CommandExit) {
            return new Message(command.getMessageToResponse());
        } else {
            command.setArgument(commandArg);
            try {
                commandExecutor.executeCommandNoScript(client, command);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (!(command instanceof CommandExecuteScript)) {
                return new Message(command.getMessageToResponse());
            }
        }
        return null;
    }


}
