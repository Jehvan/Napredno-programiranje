package Auditoriska_3;

public class Person {
    private String name;
    private String surname;

    Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Surname: %s", name, surname);
    }


}
