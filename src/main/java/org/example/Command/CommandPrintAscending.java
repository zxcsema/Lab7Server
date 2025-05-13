package org.example.Command;
import org.example.Collection.Collection;

import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public class CommandPrintAscending extends AbstractCommand implements Command {
    String result;
    public CommandPrintAscending(){
        super("print_ascending", 0);

    }
    @Override
    public void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();
        if (collection.isEmpty()) {
            result = "Коллекция пуста.";
            return;
        }

        result = collection.getAll().stream()
                .sorted().toList().toString();
    }

    @Override
    public String getDescription() {
        return "print_ascending : вывести элементы коллекции в порядке возрастания";
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
