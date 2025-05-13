package org.example.Command;
import com.google.inject.Inject;
import org.example.ClientAction.ClientConnection;
import org.example.ClientAction.ClientSession;
import org.example.Collection.Collection;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;


import javax.inject.Named;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.sql.*;
import java.util.Optional;


public class CommandUpdateId extends AbstractCommand implements Command {
    private final WorkerRepository workerRepository;
    private Long id;
    private Worker newWorker;
    private String file;
    private int line;
    String result;

    @Inject
    public CommandUpdateId(@Named("DAO") WorkerRepository workerRepository) {
        super("update_id", 1);
        this.workerRepository = workerRepository;
    }

    @Override
    public synchronized void execute(SocketChannel client, Mode mode) throws IOException, ClassNotFoundException, SQLException {
        Collection collection = Collection.getInstance();
        String currentUser = ClientConnection.getNameClient(client);

        if (Mode.SCRIPT.equals(mode)) {
            if (newWorker.isNull()) return;
        } else {
            ClientSession session = ClientConnection.getCurrentSession(client); // получаешь сессию клиента

            if (!session.isWaitingForWorker()) {
                session.setWaitingForWorker(true);
                ClientConnection.setHasDiscard(false);
                result = "interactive";
            }else{
                Optional<Worker> workerOpt = collection.getAll().stream()
                        .filter(worker -> worker.getId().equals(id))
                        .findFirst();

                if (workerOpt.isEmpty()) {
                    result = "Worker с id " + id + " не найден.";
                    session.setWaitingForWorker(false);
                    return;
                }

                Worker oldWorker = workerOpt.get();
                String owner = workerRepository.getUserLoginByWorkerId(id);

                if (!currentUser.equals(owner)) {
                    result = "Worker с id " + id + " принадлежит другому пользователю и не может быть обновлён.";

                        session.setWaitingForWorker(false);

                    return;
                }
                newWorker.setId(id);

                collection.remove(oldWorker);
                collection.add(newWorker);

                if (workerRepository.updateWorkerInDB(newWorker, client)) {
                    result = "Worker с id " + id + " успешно обновлён.";
                    session.setWaitingForWorker(false);

                } else {
                    result = "Ошибка при обновлении Worker в базе данных.";
                    session.setWaitingForWorker(false);

                }

            }
        }


    }


    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }

    @Override
    public void setScriptContext(String file, int line) {
        this.file = file;
        this.line = line;
    }

    @Override
    public void setArgument(String arguments) {
        try {
            this.id = Long.parseLong(arguments);
        } catch (NumberFormatException e) {
            System.out.println("Неверный аргумент команды: id должен быть числом.");
        }
    }

    @Override
    public int getSumOfArguments() {
        return 1;
    }

    public int getLinesToSkipInScript() {
        return 10;
    }

    @Override
    public String getMessageToResponse() {
        return result;
    }

    public void setNewWorker(Worker newWorker) {
        this.newWorker = newWorker;
    }



}