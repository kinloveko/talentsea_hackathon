package com.example.ict_congress_mock_defense.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class employeeClass implements Serializable {

    String id;
    String email;
    String name;
    String job_exp;
    String job_intro;
    Boolean isAvailable;
    String birth;
    String pass;
    String job_salary;
    String role;
    String image;
    List<String> job_skill_set = new ArrayList<>();


    public employeeClass(){

    }
    public employeeClass(String id, String email, String name, String job_exp, String job_intro, Boolean isAvailable, String birth, String pass, String job_salary, String role, String image, List<String> job_skill_set) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.job_exp = job_exp;
        this.job_intro = job_intro;
        this.isAvailable = isAvailable;
        this.birth = birth;
        this.pass = pass;
        this.job_salary = job_salary;
        this.role = role;
        this.image = image;
        this.job_skill_set = job_skill_set;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getJob_exp() {
        return job_exp;
    }

    public String getJob_intro() {
        return job_intro;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public String getBirth() {
        return birth;
    }

    public String getPass() {
        return pass;
    }

    public String getJob_salary() {
        return job_salary;
    }

    public String getRole() {
        return role;
    }

    public String getImage() {
        return image;
    }

    public List<String> getJob_skill_set() {
        return job_skill_set;
    }
}
