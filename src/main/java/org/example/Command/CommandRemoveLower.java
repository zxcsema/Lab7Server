package org.example.Command;

import com.google.inject.Inject;
import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;

import javax.inject.Named;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Optional;

public class CommandRemoveLower extends AbstractCommand implements Command {
    String result;
    private final WorkerRepository workerRepository;

    @Inject
    public CommandRemoveLower(@Named("DAO") WorkerRepository workerRepository) {
        super("remove_lower", 0);
        this.workerRepository = workerRepository;
    }

    @Override
    public synchronized void execute(SocketChannel client, Mode mode) throws SQLException {
        Collection collection = Collection.getInstance();

        if (collection.isEmpty()) {
            result = "Коллекция пуста. Удаление невозможно.";
            return;
        }

        String clientLogin = ClientConnection.getNameClient(client);

        Optional<Worker> minWorkerOpt = collection.getAll().stream()
                .filter(worker -> {
                    String owner = workerRepository.getUserLoginByWorkerId(worker.getId());
                    return clientLogin.equals(owner);
                })
                .min(Comparator.naturalOrder());

        if (minWorkerOpt.isEmpty()) {
            result = "Нет элементов, принадлежащих вам.";
            return;
        }

        Worker minWorker = minWorkerOpt.get();
        boolean dbDeleted = workerRepository.delete(minWorker.getId(), client);

        if (dbDeleted) {
            collection.getAll().remove(minWorker);
            result = "Удалён минимальный элемент, принадлежащий вам:\n" + minWorker;
        } else {
            result = "Ошибка при удалении элемента из базы данных.";
        }
    }


    @Override
    public String getDescription() {
        return "remove_min: удалить минимальный элемент, принадлежащий текущему пользователю";
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
