package org.example.Validation;

import org.example.Command.Command;
import java.util.Map;

public interface Validator {
    boolean validator(Command command, String[] parts, Map<String, Command> commands);
}
