package edu.wpi.teamname.Database;

import java.time.Instant;
import java.util.ArrayList;

public class UserRegistration {
    private int pk;
    private String name;
    private String date;
    private ArrayList<String> reasonsForVisit;
    private String phoneNumber;
    private boolean acknowledged;
    private double acknowledgedAt;
    private double submittedAt = Instant.EPOCH.getEpochSecond();
    private boolean cleared;
    private int rating;
    private String details;

    public UserRegistration(String name, String date, ArrayList<String> reasonsForVisit, String phoneNumber) {
        this.name = name;
        this.date = date;
        this.reasonsForVisit = reasonsForVisit;
        this.phoneNumber = phoneNumber;
        this.acknowledged = false;
        this.acknowledgedAt = 0.0;
        this.submittedAt = Instant.EPOCH.getEpochSecond();
        this.cleared = false;
        this.rating = -1;
        this.details = "";
    }

    public UserRegistration(int pk, String name, String date, ArrayList<String> reasonsForVisit, String phoneNumber) {
        this.pk = pk;
        this.name = name;
        this.date = date;
        this.reasonsForVisit = reasonsForVisit;
        this.phoneNumber = phoneNumber;
        this.acknowledged = false;
        this.acknowledgedAt = 0.0;
        this.submittedAt = Instant.EPOCH.getEpochSecond();
        this.cleared = false;
        this.rating = -1;
        this.details = "";
    }

    public UserRegistration(String name, String date, ArrayList<String> reasonsForVisit, String phoneNumber, boolean cleared, int rating, String details) {
        this.name = name;
        this.date = date;
        this.reasonsForVisit = reasonsForVisit;
        this.phoneNumber = phoneNumber;
        this.acknowledged = false;
        this.acknowledgedAt = 0.0;
        this.submittedAt = Instant.EPOCH.getEpochSecond();
        this.cleared = cleared;
        this.rating = rating;
        this.details = details;
    }

    public UserRegistration(String name, String date, ArrayList<String> reasonsForVisit, String phoneNumber, Boolean acknowledged, double acknowledgedAt, boolean cleared, int rating, String details) {
        this.name = name;
        this.date = date;
        this.reasonsForVisit = reasonsForVisit;
        this.phoneNumber = phoneNumber;
        this.acknowledged = acknowledged;
        this.acknowledgedAt = acknowledgedAt;
        this.cleared = cleared;
        this.rating = rating;
        this.details = details;
    }

    public UserRegistration(int pk, String name, String date, double submittedAt, ArrayList<String> reasonsForVisit, String phoneNumber, Boolean acknowledged, double acknowledgedAt, boolean cleared, int rating, String details) {
        this.pk = pk;
        this.name = name;
        this.date = date;
        this.submittedAt = submittedAt;
        this.reasonsForVisit = reasonsForVisit;
        this.phoneNumber = phoneNumber;
        this.acknowledged = acknowledged;
        this.acknowledgedAt = acknowledgedAt;
        this.cleared = cleared;
        this.rating = rating;
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public int getPk() {
        return pk;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getReasonsForVisit() {
        return reasonsForVisit;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }

    public double getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public boolean getCleared() {
        return cleared;
    }

    public void setCleared(boolean _cleared) {
        this.cleared = _cleared;
    }

    public int getRating() {
        return rating;
    }

    public String getDetails() {
        return details;
    }

    public double getSubmittedAt() {
        return submittedAt;
    }
}
