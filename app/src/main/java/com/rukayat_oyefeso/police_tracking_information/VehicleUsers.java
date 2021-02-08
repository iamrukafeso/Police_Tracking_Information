package com.rukayat_oyefeso.police_tracking_information;

public class VehicleUsers {

    //Create Vehicle Owner attributes
    private String userId;
    private String firstName;
    private String surname;
    private String email;
    private String password;
    private String dob;
    private String accountType;

    public VehicleUsers(){

    }

    public VehicleUsers(String userId, String firstName, String surname, String email, String password, String dob, String accountType) {
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.accountType = accountType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
