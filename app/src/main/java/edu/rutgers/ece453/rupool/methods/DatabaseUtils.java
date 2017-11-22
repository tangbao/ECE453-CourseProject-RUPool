package edu.rutgers.ece453.rupool.methods;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 * database methods
 *
 */

public class DatabaseUtils {
    private DatabaseReference mDatabase;

    DatabaseUtils(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

//    private void addUser(String name, String email, String passwd)




}
