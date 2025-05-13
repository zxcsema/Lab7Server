package org.example.Command;

import org.example.Collection.Collection;
import org.example.Entites.Worker;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class CommandShow extends AbstractCommand implements Command {
    private String result;
    public CommandShow(){
        super("show", 0);;
    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();
        result = collection.getAll().stream().limit(20)
                .map(worker -> worker.toString() + "\n")
                .collect(Collectors.joining());
    }

    @Override
    public String getDescription() {
        return "show : вывести " +
                "в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public String getMessageToResponse() {
        return result;
    }
}