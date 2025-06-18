package org.example.Entites;

import java.io.Serial;
import java.time.LocalDateTime;

public class NullWorker extends Worker  {
    @Serial
    private static final long serialVersionUID = 1L;
    public NullWorker() {
        super("sosal","Invalid Worker", new Coordinates(0, 0), 1.0, LocalDateTime.now(), null, Position.MANAGER, new Organization("Invalid Org", 1, OrganizationType.PUBLIC));

    }

    @Override
    public String toString() {
        return "NullWorker{}";
    }

    public boolean isNull(){
        return true;
    }
}