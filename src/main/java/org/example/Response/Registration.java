package org.example.Response;

import java.io.Serial;
import java.io.Serializable;

public class Registration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String login;
    private final String password;
    private final boolean reg;

    public Registration(String login, String password, boolean reg){
        this.reg = reg;
        this.password = password;
        this.login = login;
    }

    public boolean getReg(){
        return reg;
    }

    public String getPassword(){
        return password;
    }

    public String getLogin(){
        return login;
    }
}
