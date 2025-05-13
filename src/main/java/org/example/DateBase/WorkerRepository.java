package org.example.DateBase;

import org.example.Entites.Worker;
import org.example.Send.Sender;

import java.nio.channels.SocketChannel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface WorkerRepository {
    void reg(String login, Sender sender, SocketChannel client);
    boolean isUserExists(String login);
    boolean registerUser(String login, String password);
    boolean authenticateUser(String login, String password);
    void save(Worker worker, SocketChannel client);
    void update(Worker worker, SocketChannel client);
    boolean delete(Long id, SocketChannel client);
    String getUserLoginByWorkerId(Long id);
    boolean updateWorkerInDB(Worker worker, SocketChannel client);
    void deleteWorkersByLoginAndSaveFalse( String userLogin);
    void updateSaveStatusForAllWorkers();

}
