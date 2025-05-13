package org.example.Handler;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.ClientAction.ClientConnection;
import org.example.Command.CommandSave;
import org.example.Command.*;
import org.example.Config.BeanStorage;
import org.example.Manager.CommandManager;
import org.example.Response.Message;
import org.example.Response.Response;
import org.example.Send.Sender;
import org.example.Command.SreialazeCommand;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class CommandHandler{
    private final Sender sender;
    private final Map<String, BiConsumer<SocketChannel, SreialazeCommand>> commandMap = new HashMap<>();
    private final Response response;

    public CommandHandler( Response response) {
        this.sender = new Sender();
        this.response = response;
        initializeCommands();
    }

    private void initializeCommands() {
        commandMap.put("execute_script", this::executeScript);
        commandMap.put("exit", this::executeExit);
        commandMap.put("add", this::sendNewId);
        commandMap.put("add_if_min", this::sendNewId);
        commandMap.put("update_id", this::sendUpdateId);
    }
    private final ExecutorService executorService = Executors.newFixedThreadPool(4); // Пул потоков для отправки

    public void handleCommand(SocketChannel client, SreialazeCommand command) throws IOException, ClassNotFoundException {
        BiConsumer<SocketChannel, SreialazeCommand> commandAction = commandMap.get(command.getName());
        if (commandAction != null) {
            executorService.submit(() -> {
                try {
                    CommandManager.getInstance().getCommands().get(command.getName()).setArgument(command.getArgument());
                    commandAction.accept(client, command);
                } catch (Exception e) {
                }
            });
            return;
        }

        executorService.submit(() -> {
            try {
                sendSerializedObjectAsync(client,  response.getResponse(client, command.getName(), command.getArgument()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void executeScript(SocketChannel client, SreialazeCommand command)  {
        try {
            sendSerializedObjectAsync(client,  new Message("script/" + command.getArgument()));
            try {
                sendSerializedObjectAsync(client,  response.getResponse(client, command.getName(), command.getArgument()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void executeExit(SocketChannel client, SreialazeCommand command)  {
        ClientConnection.setHasDiscard(true);
       final BeanStorage beanStorage = new BeanStorage();
        Injector injector = Guice.createInjector(beanStorage);
        try {
            try {
                sendSerializedObjectAsync(client, response.getResponse(client, command.getName(), command.getArgument()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            injector.getInstance(CommandSave.class).execute(client, Mode.NO_SCRIPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendNewId(SocketChannel client, SreialazeCommand command) {
        try {

            try {
                sendSerializedObjectAsync(client, response.getResponse(client, command.getName(), command.getArgument()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendUpdateId(SocketChannel client, SreialazeCommand command){
        try {

            try {
                sendSerializedObjectAsync(client, response.getResponse(client, command.getName(), command.getArgument()));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void sendSerializedObjectAsync(SocketChannel client, Object obj) throws IOException {

        sender.sendSerializedObject(client, obj);

    }

}