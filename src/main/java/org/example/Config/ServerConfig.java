package org.example.Config;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sun.jdi.connect.spi.Connection;
import org.example.Collection.Collection;
import org.example.Command.*;
//import org.example.Core.ConsoleApp;
//import org.example.Exceptions.InvalidFileTypeException;
import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.example.DateBase.WorkerRepositoryJdbc;
import org.example.Manager.CommandManager;
import org.example.Manager.Manager;
import org.example.Server.Server;
//import org.example.Users.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ServerConfig {
    private final Manager commandManager;
    private final Injector injector;
    private final Server server;
    private final WorkerRepository workerRepository;

    @Inject
    public ServerConfig() throws SQLException {
        BeanStorage beanStorage = new BeanStorage();
        ConnectionDB.getConnection();
        this.injector = Guice.createInjector(beanStorage);
        this.commandManager = CommandManager.getInstance();
        this.workerRepository  = new WorkerRepositoryJdbc();
        initializeCommands();
        //this.user = injector.getInstance(User.class);
        //this.consoleApp = injector.getInstance(ConsoleApp.class);
        this.server = injector.getInstance(Server.class);

    }

    public Server getServer() {
        return server;
    }


    private void initializeCommands(){
        commandManager.registerCommand(injector.getInstance(CommandSave.class));
        commandManager.registerCommand(new CommandHelp());
        commandManager.registerCommand(new CommandInfo());
        commandManager.registerCommand(injector.getInstance(CommandExit.class));
        commandManager.registerCommand(new CommandShow());
        commandManager.registerCommand(injector.getInstance(CommandAddElement.class));
        commandManager.registerCommand(injector.getInstance(CommandAddIfMin.class));
        commandManager.registerCommand(injector.getInstance(CommandAddElement.class));
        commandManager.registerCommand(injector.getInstance(CommandRemoveById.class));
        commandManager.registerCommand(injector.getInstance(CommandUpdateId.class));
        commandManager.registerCommand( injector.getInstance(CommandRemoveAnyByStartDate.class));
        commandManager.registerCommand(new CommandPrintAscending());
        commandManager.registerCommand(injector.getInstance(CommandClear.class));
        commandManager.registerCommand(injector.getInstance(CommandRemoveHead.class));
        commandManager.registerCommand(injector.getInstance(CommandRemoveLower.class));
        commandManager.registerCommand(new CommandPrintFieldAscendingStartDate());

        commandManager.registerCommand(injector.getInstance(CommandExecuteScript.class));
    }



}