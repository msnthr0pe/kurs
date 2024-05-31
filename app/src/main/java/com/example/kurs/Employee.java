package com.example.kurs;

public class Employee {

    private String ID;
    private String name;
    private String surname;
    private String post;
    private String personalCard;
    private String employmentContract;
    private String personalDataConsent;
    private String vacationSchedule;
    private String employmentRecord;
    private String schedule;

    public String getID() {
        return ID;
    }

    public String getPersonalCard() {
        return personalCard;
    }

    public String getEmploymentContract() {
        return employmentContract;
    }

    public String getPersonalDataConsent() {
        return personalDataConsent;
    }

    public String getVacationSchedule() {
        return vacationSchedule;
    }

    public String getEmploymentRecord() {
        return employmentRecord;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getPost() {
        return post;
    }
    public Employee(){}
    public Employee(String name, String surname, String salary, String age, String post) {
        this.name = name;
        this.surname = surname;
        this.post = post;
    }
}
