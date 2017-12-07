package edu.rutgers.ece453.rupool;

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
    private int maxMember; //the max member number
    private String Date; //the date of the activity
    private String location;//todo  store Latlng?
    private double moneyPerPerson; //the money per person should pay
    private List<String> members= new ArrayList<>();; //the ids of the members who join the activity

    PoolActivity(String name, String sponsorId, int maxMember,  String Date, String location,
             double moneyPerPerson){
        this.members.add(sponsorId);

        this.name = name;
        this.sponsorId = sponsorId;
        this.maxMember = maxMember;
        this.Date = Date;
        this.location = location;
        this.moneyPerPerson = moneyPerPerson;
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
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
}
