package org.example.Manager;

import org.example.Command.Command;

import java.util.Map;

public interface Manager {
    void registerCommand(Command command);

    Map<String, Command> getCommands();
}
