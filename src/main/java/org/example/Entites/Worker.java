package org.example.Entites;



import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Worker implements Comparable<Worker>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public void setCreator(String creator) {
        this.creator = creator;
    }

    private  String creator;
    private Long id;
    private final String name;
    private final Coordinates coordinates;
    private final LocalDateTime creationDate;
    private final double salary;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final Position position;
    private Organization organization;

    public Worker(String creator, String name, Coordinates coordinates, double salary, LocalDateTime startDate,
                  LocalDateTime endDate, Position position, Organization organization) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым или null");
        if (coordinates == null) throw new IllegalArgumentException("Координаты не могут быть null");
        if (salary<=0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
        if (startDate == null) throw new IllegalArgumentException("Дата начала не может быть null");
        if (position == null) throw new IllegalArgumentException("Должность не может быть null");
        if (organization == null) throw new IllegalArgumentException("Организация не может быть null");
        this.creator = creator;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
    }

    public Worker(String creator,long id ,String name, Coordinates coordinates, double salary, LocalDateTime startDate,
                  LocalDateTime endDate, Position position, Organization organization) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым или null");
        if (coordinates == null) throw new IllegalArgumentException("Координаты не могут быть null");
        if (salary<=0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
        if (startDate == null) throw new IllegalArgumentException("Дата начала не может быть null");
        if (position == null) throw new IllegalArgumentException("Должность не может быть null");
        this.creator = creator;
        if (organization == null) throw new IllegalArgumentException("Организация не может быть null");
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
    }

    public Worker(String creator,Long id, String name, Coordinates coordinates, LocalDateTime creationDate ,double salary, LocalDateTime startDate,
                  LocalDateTime endDate, Position position, Organization organization) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя не может быть пустым или null");
        if (coordinates == null) throw new IllegalArgumentException("Координаты не могут быть null");
        if (salary<=0) throw new IllegalArgumentException("Зарплата должна быть больше 0");
        if (startDate == null) throw new IllegalArgumentException("Дата начала не может быть null");
        if (position == null) throw new IllegalArgumentException("Должность не может быть null");
        if (organization == null) throw new IllegalArgumentException("Организация не может быть null");
        this.creator = creator;

        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }



    public Coordinates getCoordinates() {
        return coordinates;
    }



    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getSalary() {
        return salary;
    }



    public LocalDateTime getStartDate() {
        return startDate;
    }


    public LocalDateTime getEndDate() {
        return endDate;
    }


    public Position getPosition() {
        return position;
    }



    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        if (organization == null) throw new IllegalArgumentException("Организация не может быть null");
        this.organization = organization;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name= " + name +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", position=" + position +
                ", organization=" + organization +
                '}';
    }

    @Override
    public int compareTo(Worker o) {
        return Double.compare(this.salary, o.salary);

    }

    public boolean isNull(){
        return false;
    }

    public String getCreator() {
        return creator;
    }
}

