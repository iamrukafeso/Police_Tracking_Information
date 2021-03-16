package com.rukayat_oyefeso.police_tracking_information;

public class model {

    String ticketID, fineType, fineNote, date, time, vFine;

    model(){

    }

    public model(String ticketID, String fineType, String fineNote, String date, String time, String vFine) {
        this.ticketID = ticketID;
        this.fineType = fineType;
        this.fineNote = fineNote;
        this.date = date;
        this.time = time;
        this.vFine = vFine;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getFineType() {
        return fineType;
    }

    public void setFineType(String fineType) {
        this.fineType = fineType;
    }

    public String getFineNote() {
        return fineNote;
    }

    public void setFineNote(String fineNote) {
        this.fineNote = fineNote;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getvFine() {
        return vFine;
    }

    public void setvFine(String vFine) {
        this.vFine = vFine;
    }
}
