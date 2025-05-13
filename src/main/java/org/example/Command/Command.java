package org.example.Command;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;

public interface Command {
    void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException;
    String getDescription();
    default void setArgument(String string) {
    }
    String getName();
    int getSumOfArguments();
    default void setScriptContext(String file, int line) {
    }

    default String getArgument() {
        return "";
    }

    default int getLinesToSkipInScript() {
        return 0; // Количество строк, которые нужно пропустить
    }
    String getMessageToResponse();
}
