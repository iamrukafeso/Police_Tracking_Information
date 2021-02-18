package com.rukayat_oyefeso.police_tracking_information;

public class Users {


    private String vehicleRegNumber;
    private String insuranceName;
    private String insuranceExpireDate;


    public Users() {
    }

    public Users(String vehicleRegNumber, String insuranceName, String insuranceExpireDate) {
        this.vehicleRegNumber = vehicleRegNumber;
        this.insuranceName = insuranceName;
        this.insuranceExpireDate = insuranceExpireDate;
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
