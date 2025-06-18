package org.example.DateBase;

import org.example.ClientAction.*;
import org.example.Entites.*;
import org.example.Response.Message;
import org.example.Send.Sender;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import org.jooq.generated.tables.Workers;
import org.jooq.generated.tables.Users;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


import static org.jooq.impl.DSL.*;

public class WorkerRepositoryJdbc implements WorkerRepository {
    private final DSLContext create;

    @Inject
    public WorkerRepositoryJdbc() throws SQLException {
        DSLContext connection = ConnectionDB.getConnection();
        this.create = ConnectionDB.getConnection(); // Указываем диалект базы данных
    }

    @Override
    public synchronized void deleteWorkersByLoginAndSaveFalse(String userLogin) {
        int rowsAffected = create.deleteFrom(Workers.WORKERS)
                .where(Workers.WORKERS.LOGIN_USER.eq(userLogin)
                        .and(Workers.WORKERS.SAVE.eq(false)))
                .execute();

        System.out.println("Удалено записей: " + rowsAffected);
    }

    @Override
    public synchronized void save(Worker worker, SocketChannel client) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


            String startDateFormatted = formatter.format(worker.getStartDate());
            String endDateFormatted = worker.getEndDate() != null
                    ? worker.getEndDate().format(formatter)
                    : null;


            var insertQuery = create.insertInto(Workers.WORKERS)
                    .set(Workers.WORKERS.NAME, worker.getName())
                    .set(Workers.WORKERS.COORDINATES_X, (int) worker.getCoordinates().getX())
                    .set(Workers.WORKERS.COORDINATES_Y, (int) worker.getCoordinates().getY())
                    .set(Workers.WORKERS.CREATION_DATE, worker.getCreationDate().toString())
                    .set(Workers.WORKERS.SALARY, worker.getSalary())
                    .set(Workers.WORKERS.START_DATE, startDateFormatted)
                    .set(Workers.WORKERS.END_DATE, endDateFormatted)  // Может быть null
                    .set(Workers.WORKERS.WORKER_POSITION, worker.getPosition().toString())
                    .set(Workers.WORKERS.ORGANIZATION_NAME, worker.getOrganization().getName())
                    .set(Workers.WORKERS.ORGANIZATION_ANNUAL, String.valueOf(worker.getOrganization().getAnnualTurnover()))
                    .set(Workers.WORKERS.ORGANIZATION_TYPE, worker.getOrganization().getType().toString())
                    .set(Workers.WORKERS.LOGIN_USER, ClientConnection.getNameClient(client))
                    .set(Workers.WORKERS.SAVE, false);

            Record record = insertQuery
                    .returning(Workers.WORKERS.ID)
                    .fetchOne();

            if (record != null) {
                worker.setId(Long.valueOf(record.get(Workers.WORKERS.ID)));
            }
        }catch (NullPointerException e){
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ашипка", e);
        }
    }


    @Override
    public synchronized void update(Worker worker, SocketChannel client) {

    }

    @Override
    public synchronized boolean delete(Long id, SocketChannel client) {
        int rows = create.deleteFrom(Workers.WORKERS)
                .where(Workers.WORKERS.ID.eq(Math.toIntExact(id))
                        .and(Workers.WORKERS.LOGIN_USER.eq(ClientConnection.getNameClient(client))))
                .execute();

        return rows > 0;
    }

    @Override
    public synchronized String getUserLoginByWorkerId(Long id) {
        Record1<String> record = create.select(Workers.WORKERS.LOGIN_USER)
                .from(Workers.WORKERS)
                .where(Workers.WORKERS.ID.eq(Math.toIntExact(id)))
                .fetchOne();

        return record != null ? record.value1() : null;
    }

    @Override
    public synchronized void updateSaveStatusForAllWorkers() {
        int rowsAffected = create.update(Workers.WORKERS)
                .set(Workers.WORKERS.SAVE, true)
                .execute();

        System.out.println("Обновлено записей: " + rowsAffected);
    }

    @Override
    public synchronized boolean updateWorkerInDB(Worker worker, SocketChannel client) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String creationDateFormatted = worker.getCreationDate().format(formatter);
        String startDateFormatted = worker.getStartDate().format(formatter);
        String endDateFormatted = worker.getEndDate().format(formatter);
        Record record = create.insertInto(Workers.WORKERS)
                .set(Workers.WORKERS.NAME, worker.getName())
                .set(Workers.WORKERS.COORDINATES_X, (int) worker.getCoordinates().getX())
                .set(Workers.WORKERS.COORDINATES_Y, (int) worker.getCoordinates().getY())
                .set(Workers.WORKERS.CREATION_DATE, creationDateFormatted)
                .set(Workers.WORKERS.SALARY, (double) worker.getSalary())
                .set(Workers.WORKERS.START_DATE, startDateFormatted)
                .set(Workers.WORKERS.END_DATE, endDateFormatted)
                .set(Workers.WORKERS.WORKER_POSITION, worker.getPosition().toString())
                .set(Workers.WORKERS.ORGANIZATION_NAME, worker.getOrganization().getName())
                .set(Workers.WORKERS.ORGANIZATION_ANNUAL, String.valueOf(worker.getOrganization().getAnnualTurnover()))
                .set(Workers.WORKERS.ORGANIZATION_TYPE, worker.getOrganization().getType().toString())
                .set(Workers.WORKERS.LOGIN_USER, ClientConnection.getNameClient(client))
                .returning(Workers.WORKERS.ID)
                .fetchOne();

        if (record != null) {
            worker.setId(Long.valueOf(record.getValue((Field<Integer>) Workers.WORKERS.ID)));
            return true;
        }

        return false;
    }

    @Override
    public synchronized void reg(String login, Sender sender, SocketChannel client) {
        try {
            boolean exists = create.fetchExists(
                    create.selectOne()
                            .from(Users.USERS)
                            .where(Users.USERS.USER_NAME.eq(login))
            );

            if (exists) {
                sender.sendSerializedObject(client, new Message("Такой логин уже занят!"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sender.sendSerializedObject(client, new Message("Ошибка при проверке логина"));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public synchronized LinkedList<Worker> loadWorkersFromDatabase() {
        LinkedList<Worker> workers = new LinkedList<>();


        var result = create.select()
                .from("workers")
                .fetch();

        for (var record : result) {
            // Извлекаем данные из записи
            String creator = record.get("login_user", String.class);
            long id = record.get("id", Long.class);
            String name = record.get("name", String.class);
            int x = record.get("coordinates_x", Integer.class);
            int y = record.get("coordinates_y", Integer.class);
            Coordinates coordinates = new Coordinates(x, y);
            LocalDateTime creationDate = record.get("creation_date", LocalDateTime.class);
            double salary = record.get("salary", Double.class);
            LocalDateTime startDate = record.get("start_date", LocalDateTime.class);
            LocalDateTime endDate = record.get("end_date", LocalDateTime.class);
            Position position = Position.valueOf(record.get("worker_position", String.class));
            String organizationName = record.get("organization_name", String.class);
            Double organizationAnnual = record.get("organization_annual", Double.class);
            OrganizationType organizationType = OrganizationType.valueOf(record.get("organization_type", String.class));
            Organization organization = new Organization(organizationName, organizationAnnual, organizationType);


            Worker worker = new Worker(creator, id, name, coordinates, creationDate, salary,
                    startDate, endDate, position, organization);

            workers.add(worker);
        }

        return workers;
    }
    @Override
    public synchronized boolean isUserExists(String login) {
        return create.selectCount()
                .from("users")
                .where(DSL.field("user_name").eq(login))
                .fetchOne(0, int.class) > 0;
    }

    @Override
    public synchronized boolean registerUser(String login, String password) {
        int rowsAffected = 0;
        try {
            rowsAffected = create.insertInto(DSL.table("users"))
                    .set(DSL.field("user_name"), login)
                    .set(DSL.field("user_password"), MD5Hashing.hashPassword(password))
                    .execute();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return rowsAffected > 0;
    }

    @Override
    public synchronized boolean authenticateUser(String login, String password) {
        try {
            return create.selectCount()
                    .from("users")
                    .where(DSL.field("user_name").eq(login))
                    .and(DSL.field("user_password").eq(MD5Hashing.hashPassword(password)))
                    .fetchOne(0, int.class) > 0;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}