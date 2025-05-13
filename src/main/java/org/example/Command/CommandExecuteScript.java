package org.example.Command;

import org.example.Manager.CommandManager;
import org.example.Manager.Manager;
import org.example.Validation.Validator;

import javax.inject.Inject;
import java.io.*;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;


public class CommandExecuteScript extends AbstractCommand implements Command{
    String savePath;
    boolean flag = true;
    @Inject
    public CommandExecuteScript(Validator validator) {
        super("execute_script", 1);
    }


    @Override
    public void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException {

    }

    @Override
    public String getDescription() {
        return "execute_script " +
                "file_name : " +
                "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, " +
                "в котором их вводит пользователь в интерактивном режиме.";
    }

    public void setArgument(String argument) {
        this.savePath = argument;
    }


    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public void setScriptContext(String file, int line) {
        this.line = line;
    }

    @Override
    public String getMessageToResponse() {
        return "";
    }
}
