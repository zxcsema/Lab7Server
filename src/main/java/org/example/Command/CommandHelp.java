package org.example.Command;

import com.google.inject.Inject;
import org.example.Manager.CommandManager;
import org.w3c.dom.ls.LSOutput;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

public class CommandHelp extends AbstractCommand implements Command {
    private final Map<String, Command> commands;
    private String result;
    @Inject
    public CommandHelp(){
        super("help", 0);
        this.commands = CommandManager.getInstance().getCommands();
    }
    @Override
    public void execute(SocketChannel client, Mode mode) {
        result = String.join("\n", commands.values().stream()
                .map(Command::getDescription)
                .toList()); // Преобразуем описания в строку с разделителем "\n"
    }

    public String getDescription(){
        return "help : вывести справку по доступным командам";
    }

    @Override
    public int getSumOfArguments() {
        return 0;
    }

    @Override
    public String getMessageToResponse() {
        return result + "\n";
    }


}
