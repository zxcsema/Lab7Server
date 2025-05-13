package org.example.Server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.Command.CommandSaveFromServer;
import org.example.Config.BeanStorage;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleListener {
    private final static String SAVE_PATH = "worker.csv";
    public void startConsoleListener() {
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            try{
                while (true) {
                    String command = scanner.nextLine().trim();
                    if (command.equals("save")){
                        BeanStorage beanStorage = new BeanStorage();
                        Injector injector = Guice.createInjector(beanStorage);
                        System.out.println("Сохранение данных...");
                        injector.getInstance(CommandSaveFromServer.class).execute();
                    }else {
                        System.out.println("неизвестная команда");
                    }
                }
            }catch (NoSuchElementException e){
                System.err.println("Остановка сервера через консоль");
                try {
                    Collection.getInstance().getAll().clear(); // Очистка коллекции
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    Files.deleteIfExists(Path.of(SAVE_PATH));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    ClientConnection.get().close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });
        consoleThread.setDaemon(true); // Поток завершится при закрытии сервера
        consoleThread.start();
    }
}
