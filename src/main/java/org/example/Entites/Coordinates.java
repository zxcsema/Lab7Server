package org.example.Entites;

import java.io.Serial;
import java.io.Serializable;

public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final Integer x; //Поле не может быть null
    private final Integer y; //Значение поля должно быть больше -420, Поле не может быть null
    public Coordinates(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int getCoordinateX(){
        return x;
    }
    public int getCoordinateY(){
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
