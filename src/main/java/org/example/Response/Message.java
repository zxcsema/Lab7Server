package org.example.Response;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String text;
    private boolean correct = true;

    public Message(String text) { this.text = text; }
    public Message(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }

    public String getText() { return text; }
    public boolean getCorrect(){
        return correct;
    }
}
