package org.example.Command;

import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.jooq.DSLContext;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.Connection;
import java.sql.SQLException;

public class CommandSaveFromServer {
    private final DSLContext DSLContext = ConnectionDB.getConnection();
    public static boolean  flag = false;
    private final WorkerRepository workerRepository;

    @Inject
    public CommandSaveFromServer(@Named("DAO") WorkerRepository workerRepository) throws SQLException {
        this.workerRepository = workerRepository;
    }

    public void execute() throws SQLException {
        workerRepository.updateSaveStatusForAllWorkers();
        flag = true;
    }



    public static boolean getSave(){

        return flag;
    }
}
