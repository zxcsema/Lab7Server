package org.example.Command;

import org.example.Collection.Collection;

import java.io.Serial;
import java.io.Serializable;

abstract public class AbstractCommand implements Command, Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    protected int line;
    protected String file;
    private Collection collection;
    String name;
    int sumOfArguments;
    public AbstractCommand(String name, int sumOfArguments){
        this.name = name;
        this.sumOfArguments = sumOfArguments;

    }

    public String getName(){
        return name;
    }


}
