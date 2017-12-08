package edu.rutgers.ece453.rupool;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 *
 */

public class User implements Serializable{
    private String uid;
    private String gender;
    private List<String> activities;

    public User(){}

    public User(String uid, String gender){
        this.uid = uid;
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities){
        this.activities = activities;
    }

    public void joinActivity(String activity_id) {
        activities.add(activity_id);
    }

    public void quitActivity(String activity_id) {
        activities.remove(activity_id);
    }


}
