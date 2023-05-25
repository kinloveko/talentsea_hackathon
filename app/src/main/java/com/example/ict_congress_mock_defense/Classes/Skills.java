package com.example.ict_congress_mock_defense.Classes;

import java.io.Serializable;

public class Skills implements Serializable {


    String skills;

    public Skills(String skills) {

        this.skills = skills;

    }

    public String getSkills() {
        return skills;
    }
}
