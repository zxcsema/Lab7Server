package org.example.Handler;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.ClientAction.ClientConnection;
import org.example.ClientAction.ClientSession;
import org.example.Collection.Collection;
import org.example.CollectionUpdate.CollectionUpdate;
import org.example.Command.*;
import org.example.Config.BeanStorage;
import org.example.DateBase.ConnectionDB;
import org.example.Entites.Worker;
import org.example.Response.Registration;
import org.example.Send.Sender;
import org.example.UpdaterCollection;

public class ObjectHandler {
    private final Map<Class<?>, BiConsumer<SocketChannel, Object>> handlers = new HashMap<>();
    private String lastCommandName = String.valueOf(new AtomicReference<>());
    private final CommandHandler commandHandler;
    private final WorkerCommandHandler workerCommandHandler;
    BeanStorage beanStorage = new BeanStorage();
        Injector injector = Guice.createInjector(beanStorage);
    private final RegistrationHandler registrationHandler = injector.getInstance(RegistrationHandler.class);
    private final ExecutorService writeExecutor = Executors.newFixedThreadPool(3);
    public ObjectHandler(CommandHandler commandHandler, WorkerCommandHandler workerCommandHandler) {
        this.commandHandler = commandHandler;
        this.workerCommandHandler = workerCommandHandler;
        initHandlers();
    }

    private void initHandlers(){
        handlers.put(SreialazeCommand.class, (client, obj) -> {
            SreialazeCommand command = (SreialazeCommand) obj;
            ClientConnection.getCurrentSession(client).setLastCommandName(command.getName());
            ClientConnection.setMode(client, command.getScript());
            lastCommandName = command.getName();
            try {
                commandHandler.handleCommand(client, command);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        handlers.put(Worker.class, (client, obj) -> {
            Worker worker = (Worker) obj;
            workerCommandHandler.handleCommand(lastCommandName, worker, client);
            UpdaterCollection.update();
        });

        handlers.put(Registration.class, (client, obj) -> {
            Registration registration = (Registration) obj;
            try {
                registrationHandler.reg(registration, client);
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });

        handlers.put(CollectionUpdate.class, (client, obj) -> {
            try {
                new UpdateCollectionHandler().sendSerializedObjectAsync(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });


    }

    public  void handleObject(SocketChannel client, Object obj){
        if (obj == null) {
            System.err.println("Ошибка: получен null-объект");
            return;
        }

        BiConsumer<SocketChannel, Object> handler = handlers.get(obj.getClass());

        if (handler == null) {
            System.err.println("Ошибка: неизвестный тип объекта " + obj.getClass());
            return;
        }
        try {
            writeExecutor.submit(()->{
                handler.accept(client, obj);
            });
        } catch (Exception e) {
            System.err.println("Ошибка обработки объекта: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
