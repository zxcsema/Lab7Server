package org.example.Command;

import org.example.Collection.Collection;

import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class CommandInfo extends AbstractCommand implements Command {
    private String result;
    public CommandInfo(){
        super("info", 0);

    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();
        result = "Тип коллекции: " + collection.getClass().getName() +
                ", Дата инициализации: " + collection.getInitializationDate() +
                ", Количество элементов: " + collection.size();
    }

    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции " +
                "(тип, дата инициализации, количество элементов и т.д.)";
    }

    @Override
    public int getSumOfArguments() {
        return  sumOfArguments;
    }


    @Override
    public String getMessageToResponse() {
        return result + "\n";
    }
}
