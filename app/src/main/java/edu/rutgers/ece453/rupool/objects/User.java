package edu.rutgers.ece453.rupool.objects;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 *
 */

public class User{
    private String gender;
    private List<String> activities = new ArrayList<>();

    public User(){}

    public User(String gender){
        this.gender = gender;
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

    public void updateActivities(String activity_id) {
        activities.add(activity_id);
    }


}
