package org.example.Collection;

import org.example.DateBase.ConnectionDB;
import org.example.DateBase.WorkerRepository;
import org.example.DateBase.WorkerRepositoryJdbc;
import org.example.Entites.*;
import org.jooq.DSLContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Collection {
    private static Collection instance;
    private LinkedList<Worker> collection;
    private final Date initializationDate;
    private final ReentrantLock lock = new ReentrantLock();
    private boolean isInitialized = false; // Флаг для отслеживания инициализации
    private final WorkerRepositoryJdbc workerRepository = new WorkerRepositoryJdbc();
    DSLContext conn = ConnectionDB.getConnection();
    private Collection() throws SQLException {
        initializeFromDatabase();
        this.initializationDate = new Date();
    }

    public static synchronized Collection getInstance() throws SQLException {
        if(instance == null){

            instance = new Collection();

        }
        return instance;
    }

    private void initializeFromDatabase() {
        lock.lock();
        try {
            if (!isInitialized) {

                collection = loadWorkersFromDatabase();
                isInitialized = true;
            }
        } finally {
            lock.unlock();
        }
    }


    // Метод для загрузки работников из базы данных
    private LinkedList<Worker> loadWorkersFromDatabase() {
        LinkedList<Worker> workers = workerRepository.loadWorkersFromDatabase();
        return workers;
    }

    public void add(Worker worker) {
        lock.lock();
        try {
            collection.add(worker);
        } finally {
            lock.unlock();
        }
    }

    public void remove(Worker worker){
        lock.lock();
        try {
            collection.remove(worker);
        } finally {
            lock.unlock();
        }
    }

    public List<Worker> getAll() {
        lock.lock();
        try {
            return new LinkedList<>(collection); // возвращаем копию
        } finally {
            lock.unlock();
        }
    }

    public Worker removeHead() {
        lock.lock();
        try {
            return collection.pollFirst(); // null если пусто
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return collection.isEmpty(); // возвращаем копию
        } finally {
            lock.unlock();
        }
    }

    public Worker getFirst() {
        lock.lock();
        try {
            return collection.getFirst(); // возвращаем копию
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return collection.size();
        } finally {
            lock.unlock();
        }
    }

    public void clear() {
        lock.lock();
        try {
            collection = loadWorkersFromDatabase();
        } finally {
            lock.unlock();
        }
    }

    public Date getInitializationDate(){
        return initializationDate;
    }
}
