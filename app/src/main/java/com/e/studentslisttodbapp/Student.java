package com.e.studentslisttodbapp;

import java.io.Serializable;

public class Student implements /*Parcelable,*/ Serializable {
    private int id;
    private String firstName;
    private String secondName;
    private String groupeNumber;
    private int avatar;
    private Boolean isChecked;

    Student(int id, String firstName, String secondName,
            String groupeNumber, Boolean isMale){
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.groupeNumber = groupeNumber;
        if(isMale){
            this.avatar = R.drawable.male_profile;
        }else{
            this.avatar = R.drawable.female_profile;
        }
        this.isChecked = false;
    }
    Student(){}

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return this.secondName;
    }
    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getGroupeNumber() {
        return this.groupeNumber;
    }
    public void setGroupeNumber(String groupeNumber) {
        this.groupeNumber = groupeNumber;
    }

    public int getAvatar() {
        return this.avatar;
    }
    public void setAvatar(Boolean isMale) {
        if(isMale){
            this.avatar = R.drawable.male_profile;
        }else{
            this.avatar = R.drawable.female_profile;
        }
    }
    public Boolean getIsChecked() {
        return this.isChecked;
    }
    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
