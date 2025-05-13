package org.example.Handler;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.example.ClientAction.ClientConnection;
import org.example.DateBase.ConnectionDB;
import org.example.DateBase.MD5Hashing;
import org.example.DateBase.WorkerRepository;
import org.example.Response.Message;
import org.example.Response.Registration;
import org.example.Send.Sender;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegistrationHandler {
    private final Sender sender = new Sender();
    private final WorkerRepository workerRepository;


    @Inject
    public RegistrationHandler(@Named("DAO") WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;

    }

    public void reg(Registration message, SocketChannel client) throws IOException, NoSuchAlgorithmException {
        String login = message.getLogin();
        String password = message.getPassword();
        boolean reg = message.getReg();

        if (reg) {
            if (workerRepository.isUserExists(login)) {
                sender.sendSerializedObject(client, new Message("Такой логин уже занят!"));
                return;
            }

            if (workerRepository.registerUser(login, password)) {
                sender.sendSerializedObject(client, new Message("Добро пожаловать, " + login + "!"));
                ClientConnection.registerNameClient(client, login);
            } else {
                sender.sendSerializedObject(client, new Message("Ошибка регистрации"));
            }
        } else {
            if (workerRepository.authenticateUser(login, password)) {
                sender.sendSerializedObject(client, new Message("Добро пожаловать, " + login + "!"));
                ClientConnection.registerNameClient(client, login);
            } else {
                sender.sendSerializedObject(client, new Message("Неверный логин или пароль"));
            }
        }
    }
}
