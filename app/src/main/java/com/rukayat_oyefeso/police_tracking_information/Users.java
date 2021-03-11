package com.rukayat_oyefeso.police_tracking_information;

public class Users {


    private String vehicleRegNumber;
    private String insuranceName;
    private String insuranceExpireDate;
    private String roadTaxValidDate;
    private String nctValidDate;
    private String vehicleOwnerAddress;
    private String firstName;
    private String surname;
    private String image;

    public Users() {
    }

    public Users(String vehicleRegNumber, String insuranceName, String insuranceExpireDate, String roadTaxValidDate, String nctValidDate, String vehicleOwnerAddress, String firstName, String surname, String image) {
        this.vehicleRegNumber = vehicleRegNumber;
        this.insuranceName = insuranceName;
        this.insuranceExpireDate = insuranceExpireDate;
        this.roadTaxValidDate = roadTaxValidDate;
        this.nctValidDate = nctValidDate;
        this.vehicleOwnerAddress = vehicleOwnerAddress;
        this.firstName = firstName;
        this.surname = surname;
        this.image = image;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public String getInsuranceExpireDate() {
        return insuranceExpireDate;
    }

    public void setInsuranceExpireDate(String insuranceExpireDate) {
        this.insuranceExpireDate = insuranceExpireDate;
    }

    public String getRoadTaxValidDate() {
        return roadTaxValidDate;
    }

    public void setRoadTaxValidDate(String roadTaxValidDate) {
        this.roadTaxValidDate = roadTaxValidDate;
    }

    public String getNctValidDate() {
        return nctValidDate;
    }

    public void setNctValidDate(String nctValidDate) {
        this.nctValidDate = nctValidDate;
    }

    public String getVehicleOwnerAddress() {
        return vehicleOwnerAddress;
    }

    public void setVehicleOwnerAddress(String vehicleOwnerAddress) {
        this.vehicleOwnerAddress = vehicleOwnerAddress;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //    //Create Vehicle Owner attributes
//    private String firstName;
//    private String image;
//    private String vehicleRegNumber;
//    private String insuranceName;
//
//    public Users(){
//
//    }
//
//    public Users(String firstName, String image, String vehicleRegNumber, String insuranceName) {
//        this.firstName = firstName;
//        this.image = image;
//        this.vehicleRegNumber = vehicleRegNumber;
//        this.insuranceName = insuranceName;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getVehicleRegNumber() {
//        return vehicleRegNumber;
//    }
//
//    public void setVehicleRegNumber(String vehicleRegNumber) {
//        this.vehicleRegNumber = vehicleRegNumber;
//    }
//
//    public String getInsuranceName() {
//        return insuranceName;
//    }
//
//    public void setInsuranceName(String insuranceName) {
//        this.insuranceName = insuranceName;
//    }
}
