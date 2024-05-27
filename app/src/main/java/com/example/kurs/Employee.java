package com.example.kurs;

public class Employee {

    private String name;
    private String surname;
    private String salary;
    private String age;
    private String post;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSalary() {
        return salary;
    }

    public String getAge() {
        return age;
    }

    public String getPost() {return post;}
    public Employee(){

    }
    public Employee(String name, String surname, String salary, String age, String post) {
        this.name = name;
        this.surname = surname;
        this.salary = salary;
        this.age = age;
        this.post = post;
    }
}
