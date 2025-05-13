package org.example.Entites;


import java.io.Serial;
import java.io.Serializable;

public class Organization  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String fullName; // Длина строки не должна быть больше 1646, строка не может быть пустой, поле может быть null
    private double annualTurnover; // Значение поля должно быть больше 0
    private OrganizationType type; // Поле не может быть null


    public Organization(String fullName, double annualTurnover, OrganizationType type) {
        setFullName(fullName);
        setAnnualTurnover(annualTurnover);
        setType(type);
    }


    public String getFullName() {
        return fullName;
    }

    public double getAnnualTurnover() {
        return annualTurnover;
    }

    public OrganizationType getType() {
        return type;
    }


    public void setFullName(String fullName) {
        if (fullName != null && (fullName.isEmpty() || fullName.length() > 1646)) {
            throw new IllegalArgumentException("ашипка");
        }
        this.fullName = fullName;
    }

    public void setAnnualTurnover(double annualTurnover) {
        if (annualTurnover <= 0) {
            throw new IllegalArgumentException("ашипка");
        }
        this.annualTurnover = annualTurnover;
    }

    public void setType(OrganizationType type) {
        if (type == null) {
            throw new IllegalArgumentException("ашипка");
        }
        this.type = type;
    }
    @Override
    public String toString() {
        return "Organization{" +
                "fullName=" + fullName +
                ", annualTurnover=" + annualTurnover +
                ", OrganizationType=" + type+
                '}';
    }

    public String getName() {
        return fullName;
    }


}
