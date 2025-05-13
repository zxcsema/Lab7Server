package org.example.Command;

import org.example.Collection.Collection;
import org.example.Entites.Worker;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class CommandPrintFieldAscendingStartDate extends AbstractCommand implements Command {
    String result;

    public CommandPrintFieldAscendingStartDate(){
        super("print_field_ascending_start_date", 0);
    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();
        if (collection.isEmpty()) {
            result = "Коллекция пуста.";
            return;
        }

        result = collection.getAll().stream().map(Worker::getStartDate).sorted()
                .toList()
                .toString();
    }

    @Override
    public String getDescription() {
        return "print_field_ascending_start_date : " +
                "вывести значения поля startDate всех элементов в порядке возрастания";
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
