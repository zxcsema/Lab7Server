package org.example.ClientAction;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.Collection.Collection;
import org.example.Command.Command;
import org.example.Command.CommandExit;
import org.example.Command.CommandSave;
import org.example.Command.Mode;
import org.example.Config.BeanStorage;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

public class ClientShutdown {


    public ClientShutdown() {
    }


    public void clientShutdown(SocketChannel client) throws IOException, SQLException, ClassNotFoundException {
      BeanStorage beanStorage = new BeanStorage();
        Injector injector = Guice.createInjector(beanStorage);
        Command command = injector.getInstance(CommandSave.class);
        System.out.println("Клиент отключился " + client.getLocalAddress());
        command.execute(client, Mode.NO_SCRIPT);
        ClientConnection.registerDiscardClient(client);
        client.close();
    }
}
