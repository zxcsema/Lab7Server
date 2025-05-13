package org.example.Command;

import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Iterator;

public class CommandRemoveHead extends AbstractCommand implements Command {
    String result;
    private final WorkerRepository workerRepository;
    @Inject
    public CommandRemoveHead(@Named("DAO") WorkerRepository workerRepository) {
        super("remove_head", 0);

        this.workerRepository =  workerRepository;
    }

    @Override
    public synchronized void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();

        if (collection.isEmpty()) {
            result = "Коллекция пуста. Удаление невозможно.";
            return;
        }

        String clientLogin = ClientConnection.getNameClient(client);
        Worker foundWorker = null;

        Iterator<Worker> iterator = collection.getAll().iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            String owner = workerRepository.getUserLoginByWorkerId(worker.getId());
            if (clientLogin.equals(owner)) {
                foundWorker = worker;
                break;
            }
        }

        if (foundWorker == null) {
            result = "В коллекции нет элементов, принадлежащих вам.";
            return;
        }

        boolean dbDeleted = workerRepository.delete(foundWorker.getId(), client);
        if (dbDeleted) {
            collection.remove(foundWorker);
            result = "Удалён первый элемент, принадлежащий вам:\n" + foundWorker;
        } else {
            result = "Ошибка при удалении элемента из базы данных.";
        }
    }




    @Override
    public String getDescription() {
        return "remove_head : удалить первый элемент коллекции, принадлежащий текущему пользователю";
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
