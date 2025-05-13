package org.example.Command;

import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;
import org.postgresql.gss.GSSOutputStream;
import org.w3c.dom.ls.LSOutput;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.ArrayList;

public class CommandRemoveById extends AbstractCommand implements Command {
    String result;
    private Long id;
private  final  WorkerRepository workerRepository;
@Inject
    public CommandRemoveById(@Named("DAO")WorkerRepository workerRepository) {
        super("remove_by_id", 1);
        this.workerRepository = workerRepository;
    }

    @Override
    public synchronized void execute(SocketChannel client, Mode mode) throws SQLException {

        Collection collection = Collection.getInstance();
        if (id == null || id == -1) {
            result = "Неверный ID.";
            return;
        }

        String clientLogin = ClientConnection.getNameClient(client);
        String dbLogin = workerRepository.getUserLoginByWorkerId(id);

        if (dbLogin == null) {
            result = "Worker с id " + id + " не найден в базе.";
            return;
        }

        if (!dbLogin.equals(clientLogin)) {
            result = "Worker с id " + id + " не принадлежит вам.";
            return;
        }

        boolean dbDeleted = workerRepository.delete(id, client);
        if (dbDeleted) {
            for (Worker worker : new ArrayList<>(collection.getAll())) {
                if (worker.getId().equals(id)) {
                    collection.remove(worker);
                    break;
                }
            }
            result = "Worker с id " + id + " успешно удалён.";
        } else {
            result = "Ошибка при удалении Worker с id " + id + " из базы.";
        }
    }




    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id (только если он принадлежит вам)";
    }

    @Override
    public void setArgument(String arguments) {
        try {
            this.id = Long.parseLong(arguments);
        } catch (NumberFormatException e) {
            result = "Неверный аргумент команды " + getName();
            this.id = -1L;
        }
    }

    @Override
    public int getSumOfArguments() {
        return sumOfArguments;
    }

    @Override
    public String getMessageToResponse() {
        return result;
    }
}
