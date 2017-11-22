package edu.rutgers.ece453.rupool.objects;

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
    private String id;
    private String name;
    private String email;
    private boolean status = false;
    private List<Map<String, Object>> activities = new ArrayList<>();

    public User(){
    }

    public User(String id, String name, String email){
        this.name = name;
        this.email = email;
    }




}
