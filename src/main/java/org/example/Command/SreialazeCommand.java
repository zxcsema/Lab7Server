package org.example.Command;

import java.io.Serial;
import java.io.Serializable;

public class SreialazeCommand implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private final String argument;
    private final boolean script;
    String name;
    int sumOfArguments;
    public SreialazeCommand(String name, String argument,int sumOfArguments, boolean script){
        this.name = name;
        this.argument = argument;
        this.sumOfArguments = sumOfArguments;
        this.script = script;
    }

    public String getName(){
        return name;
    }
    public String getArgument(){
        return argument;
    }
    public int getSumOfArguments(){
        return sumOfArguments;
    }

    public boolean getScript(){
        return script;
    }
}
