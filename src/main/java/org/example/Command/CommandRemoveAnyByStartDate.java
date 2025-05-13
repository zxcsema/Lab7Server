package org.example.Command;
import org.example.ClientAction.ClientConnection;
import org.example.Collection.Collection;
import org.example.DateBase.WorkerRepository;
import org.example.Entites.Worker;

import javax.inject.Inject;
import javax.inject.Named;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommandRemoveAnyByStartDate extends AbstractCommand implements Command {
    String result;
    private LocalDateTime startDate;
    private final WorkerRepository workerRepository;

    @Inject
    public CommandRemoveAnyByStartDate(@Named("DAO") WorkerRepository workerRepository) throws SQLException {
        super("remove_any_by_start_date", 1);
        this.workerRepository =workerRepository;
    }
    @Override
    public  synchronized void execute(SocketChannel client, Mode mode) throws SQLException {

        Collection collection = Collection.getInstance();
        if (startDate == null) {
            result = "Дата начала не указана.";
            return;
        }

        // Находим всех работников с заданной startDate
        List<Worker> workersToRemove = collection.getAll().stream()
                .filter(worker -> worker.getStartDate().equals(startDate))
                .toList();
        System.out.println(workersToRemove);

        if (workersToRemove.isEmpty()) {
            result = "Элементы с startDate " + startDate + " не найдены.";
            return;
        }

        boolean workerFound = false;
        for (Worker worker : workersToRemove) {
            String userLogin = workerRepository.getUserLoginByWorkerId(worker.getId());
            if (userLogin != null && userLogin.equals(ClientConnection.getNameClient(client))) {
                workerRepository.delete(worker.getId(), client);
                collection.remove(worker);
                result = "Удалён элемент с startDate: " + startDate + " и логином пользователя: " + userLogin;
                workerFound = true;
                break;
            }
        }

        if (!workerFound) {
            result = "Элемент с startDate " + startDate + " не принадлежит вам.";
        }
    }


    // what the noisiest city in the world, what is the foggiest city in Europe, which city has the worst traffic in the world
    //






    @Override
    public String getDescription() {
        return "remove_any_by_start_date startDate : удалить из коллекции один элемент, значение поля startDate которого эквивалентно заданному";
    }

    @Override
    public void setArgument(String arguments) {
        try{
            this.startDate = LocalDateTime.parse(arguments, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }catch(RuntimeException e){
            System.out.println("неверные аргументы команды " + getName());
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
