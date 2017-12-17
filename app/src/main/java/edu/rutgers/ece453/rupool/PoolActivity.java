package edu.rutgers.ece453.rupool;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 *
 */

public class PoolActivity implements Serializable {
    private String id; //the id of the activity
    private String name; //the name of the activity
    private String sponsorId;//the uid of the activity sponsor
    private String date; //the date of the activity
    private String description; //the description of the activity
    private String startPoint;
    private String destiName; //the name of the destination
    private String destiAddress; //the address of the destination
    private double destiLatitude; //the LatLng of the destination
    private double destiLongitude;
    private int maxMember; //the max member number
    private double moneyPerPerson; //the money per person should pay
    private List<String> members= new ArrayList<>(); //the ids of the members who join the activity
    private boolean status;

    PoolActivity(){}

    PoolActivity(String name, String sponsorId, String date, String description,
                 String startPoint, int maxMember, double moneyPerPerson){
        this.members.add(sponsorId);

        this.name = name;
        this.sponsorId = sponsorId;
        this.maxMember = maxMember;
        this.date = date;
        this.description = description;
        this.startPoint = startPoint;
        this.moneyPerPerson = moneyPerPerson;
        this.status = true;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public String getSponsorId(){
        return sponsorId;
    }

    public int getMaxMember() {
        return maxMember;
    }

    public void setMaxMember(int maxMember) {
        this.maxMember = maxMember;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String d) {
        date = d;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public double getDestiLatitude() {
        return destiLatitude;
    }

    public double getDestiLongitude() {
        return destiLongitude;
    }

    public String getDestiAddress() {
        return destiAddress;
    }

    public String getDestiName() {
        return destiName;
    }

    public void setPlace(Place place) {
        this.destiName = place.getName().toString();
        this.destiAddress = place.getAddress().toString();
        this.destiLongitude = place.getLatLng().longitude;
        this.destiLatitude = place.getLatLng().latitude;
    }

    public double getMoneyPerPerson() {
        return moneyPerPerson;
    }

    public void setMoneyPerPerson(double moneyPerPerson) {
        this.moneyPerPerson = moneyPerPerson;
    }

    public List<String> getMembers() {
        return members;
    }

    public void addMember(String uid) {
        this.members.add(uid);
    }

    public void removeMember(String uid){
        this.members.remove(uid);
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus(){
        return status;
    }
}
