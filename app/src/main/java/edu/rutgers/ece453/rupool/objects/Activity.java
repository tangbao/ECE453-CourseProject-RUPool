package edu.rutgers.ece453.rupool.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 *
 */

public class Activity {
    private String id; //the id of the activity
    private String name; //the name of the activity
    private String sponsor_id;//the uid of the activity sponsor
    private int maxMember; //the max member number
    private String Date; //the date of the activity
    private String location;//todo  store Latlng?
    private double moneyPerPerson; //the money per person should pay
    private List<String> members= new ArrayList<>();; //the ids of the members who join the activity

    Activity(String name, String sponsor_id, int maxMember,  String Date, String location,
             double moneyPerPerson){
        this.members.add(sponsor_id);

        this.name = name;
        this.sponsor_id = sponsor_id;
        this.maxMember = maxMember;
        this.Date = Date;
        this.location = location;
        this.moneyPerPerson = moneyPerPerson;
    }

}
