package com.example.ict_congress_mock_defense.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class companyClass implements Serializable {
    String id;
    String company;
    String department;
    String email;
    Boolean isAvailable;
    String birth;
    String pass;
    String salaryRange;
    String role;
    String image;
    List<String> isLookingFor = new ArrayList<>();
    public companyClass(){

    }

    public companyClass(String id, String image,String company,    List<String> isLookingFor , String department, String email, Boolean isAvailable, String birth, String pass, String salaryRange, String role) {
        this.id = id;
        this.isLookingFor = isLookingFor;
        this.company = company;
        this.department = department;
        this.email = email;
        this.image = image;
        this.isAvailable = isAvailable;
        this.birth = birth;
        this.pass = pass;
        this.salaryRange = salaryRange;
        this.role = role;
    }

    public List<String> getIsLookingFor() {
        return isLookingFor;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
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

    public String getSalaryRange() {
        return salaryRange;
    }

    public String getRole() {
        return role;
    }

}
