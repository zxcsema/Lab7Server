package org.example.Manager;

import org.example.Command.Command;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;



public class CommandManager implements Manager {
    // Единственный экземпляр класса
    private static final CommandManager INSTANCE = new CommandManager();

    // Коллекция для хранения команд
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, Integer> commandMode = new HashMap<>();

    // Приватный конструктор, чтобы предотвратить создание экземпляров извне
    private CommandManager() {}

    // Метод для получения единственного экземпляра
    public static CommandManager getInstance() {
        return INSTANCE;
    }

    // Регистрация команды
    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
        commandMode.put(command.getName(), command.getSumOfArguments());
    }

    // Получение всех команд
    public Map<String, Command> getCommands() {
        return commands;
    }
}

