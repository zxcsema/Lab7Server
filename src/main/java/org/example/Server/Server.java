package org.example.Server;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.example.ClientAction.ClientConnection;
import org.example.ClientAction.ClientShutdown;
import org.example.Command.*;
import org.example.Config.BeanStorage;
import org.example.Handler.CommandHandler;
import org.example.Handler.ObjectHandler;
import org.example.Handler.WorkerCommandHandler;
import org.example.Response.Response;
import org.example.Send.Sender;
import org.example.Reader.SocketObjectReader;
import org.example.Collection.Collection;
import java.net.InetSocketAddress;
import java.io.*;
import java.nio.channels.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class Server {
    private final Map<Class<?>, BiConsumer<SocketChannel, Object>> handlers = new HashMap<>();
    private final Selector selector;
    private final ServerSocketChannel serverChannel;
    private final BeanStorage beanStorage = new BeanStorage();
    private final Injector injector = Guice.createInjector(beanStorage);
    private final ConsoleListener consoleListener;
    private final SocketObjectReader objectReader;
    private final ObjectHandler objectHandler;
    CommandAddElement commandAddElement = injector.getInstance(CommandAddElement.class);
    CommandAddIfMin commandAddIfMin = injector.getInstance(CommandAddIfMin.class);
    CommandUpdateId commandUpdateId = injector.getInstance(CommandUpdateId.class);
    WorkerCommandHandler workerCommandHandler;
    Response response = new Response();
    private final CommandHandler commandHandler;
    private final ExecutorService objectHandlerExecutor = Executors.newFixedThreadPool(3);
    @Inject
    public Server(ConsoleListener consoleListener, Sender sendSerializedObject) throws IOException, SQLException {
        this.consoleListener = consoleListener;
        this.selector = Selector.open();
        this.serverChannel = ServerSocketChannel.open();
        this.serverChannel.bind(new InetSocketAddress("localhost",0));
        InetSocketAddress address = (InetSocketAddress) serverChannel.getLocalAddress();
        int port = address.getPort();
        System.out.println("Server is listening on port: " + port);
        this.serverChannel.configureBlocking(false);
        this.serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.workerCommandHandler = new WorkerCommandHandler(commandAddElement, commandAddIfMin, commandUpdateId, sendSerializedObject);
        this.commandHandler = new CommandHandler(response);
        ClientShutdown clientShutdown = new ClientShutdown();
        this.objectReader = new SocketObjectReader(clientShutdown);
        this.objectHandler = new ObjectHandler(commandHandler, workerCommandHandler);
    }
    public void startServer() throws IOException, ClassNotFoundException, SQLException {
        consoleListener.startConsoleListener();
        while (true) {
            selector.select();
            Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (key.isAcceptable()) {
                    SocketChannel client = serverChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    ClientConnection.registerClient(client);
                    new Sender().sendSerializedObject(client , Collection.getInstance().getAll());
                    Collection.getInstance();
                    System.out.println("Новое подключение: " + client.getRemoteAddress());
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    byte[] objectData = readSerializedObject(client);
                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(objectData))) {
                        Object obj = ois.readObject();
                        objectHandlerExecutor.submit(()->{
                            objectHandler.handleObject(client, obj);
                        });
                    }catch (NullPointerException ignore){

                    }
                }
            }
        }
    }


    private  byte[] readSerializedObject(SocketChannel client) throws IOException, SQLException, ClassNotFoundException {
        return objectReader.readSerializedObject(client);
    }
}
