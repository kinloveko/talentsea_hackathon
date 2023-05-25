package com.example.ict_congress_mock_defense.Classes;

import com.google.firebase.Timestamp;

import java.io.Serializable;


public class timestampClass implements Serializable {

    Timestamp time;
    String id;
    String time_id;
    public timestampClass(){

    }

    public timestampClass(Timestamp time,String id,String time_id){
        this.time = time;
        this.id = id;
        this.time_id = time_id;
    }

    public String getTime_id() {
        return time_id;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
