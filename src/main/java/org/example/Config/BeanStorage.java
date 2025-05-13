package org.example.Config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.example.ClientAction.ClientShutdown;
import org.example.Command.Command;
import org.example.Command.CommandSave;
import org.example.DateBase.WorkerRepository;
import org.example.DateBase.WorkerRepositoryJdbc;
import org.example.Server.ConsoleListener;
import org.example.Validation.ValidatorForCommands;
import org.example.Validation.Validator;
import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class BeanStorage extends AbstractModule {
    @Override
    public void configure() {
        bind(Validator.class).to(ValidatorForCommands.class);
        bind(Command.class).annotatedWith(Names.named("save")).to(CommandSave.class);
        bind(Selector.class).toInstance(createSelector());
        bind(ServerSocketChannel.class).toInstance(createServerSocketChannel());
        bind(BeanStorage.class).toInstance(this);
        bind(ConsoleListener.class).toInstance(createInstance(ConsoleListener.class));
        bind(ClientShutdown.class).toInstance(createInstance(ClientShutdown.class));
        bind(WorkerRepository.class).annotatedWith(Names.named("DAO")).to(WorkerRepositoryJdbc.class);
    }

    // Метод для создания объекта Selector
    private Selector createSelector() {
        try {
            return Selector.open();
        } catch (IOException e) {
            throw new RuntimeException("Error creating Selector", e);
        }
    }

    // Метод для создания объекта ServerSocketChannel
    private ServerSocketChannel createServerSocketChannel() {
        try {
            return ServerSocketChannel.open();
        } catch (IOException e) {
            throw new RuntimeException("Error creating ServerSocketChannel", e);
        }
    }

    // Универсальный метод для создания экземпляров классов с исключениями
    private <T> T createInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Error creating instance of " + clazz.getName(), e);
        }
    }
}
