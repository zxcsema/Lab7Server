package org.example.Validation;

import org.example.Command.Command;
import java.util.Map;

public class ValidatorForCommands implements Validator {


    public  boolean validator(Command command, String[] parts, Map<String, Command> commands){
        if (parts[0].trim().isEmpty()) {
            return false;
        }
        if(!commands.containsKey(parts[0])){
            return false;
        }

        return parts.length - 1 == command.getSumOfArguments();
    }
}
