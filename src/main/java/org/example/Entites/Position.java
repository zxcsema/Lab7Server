package org.example.Entites;

import java.io.Serial;
import java.io.Serializable;

public enum Position implements Serializable {
    DIRECTOR,
    MANAGER,
    ENGINEER,
    DEVELOPER,
    COOK;
    @Serial
    private static final long serialVersionUID = 1L;
}
