package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.rutgers.ece453.rupool.Interface.*;

/**
 * Created by Zhongze Tang on 2017/11/21.
 *
 * database methods
 *
 */

class DatabaseUtils {

    private String TAG = "DatabaseUtils";

    private OnGetUserListener mGetUserListener;
    private OnGetActivityListener mGetActivityListener;

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersRef;
    private DatabaseReference mActivRef;

    DatabaseUtils(Context context){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mDatabase.child("users");
        mActivRef = mDatabase.child("activities");

        mGetUserListener = (OnGetUserListener) context;
        mGetActivityListener = (OnGetActivityListener) context;
    }

    void addUser(String uid, User user){
        mUsersRef.child(uid).setValue(user);
    }

    void getUser(String uid){
        DatabaseReference mUserRef = mUsersRef.child(uid);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mGetUserListener.onGetUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        });
    }

    void addActivity(PoolActivity pa){
        String activityId = mActivRef.push().getKey();
        mActivRef.child(activityId).setValue(pa);
    }

    void getActivity(String aid){
        DatabaseReference mAcRef = mActivRef.child(aid);
        mAcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PoolActivity pa = dataSnapshot.getValue(PoolActivity.class);
                mGetActivityListener.onGetActivity(pa);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadActivity:onCancelled", databaseError.toException());
            }
        });
    }

}
