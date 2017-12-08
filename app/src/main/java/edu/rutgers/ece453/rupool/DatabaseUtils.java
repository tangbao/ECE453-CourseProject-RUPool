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

    void addUser(User user){
        mUsersRef.child(user.getUid()).setValue(user);
    }

    void getUser(String uid, final int ACTION_CODE){
        DatabaseReference mUserRef = mUsersRef.child(uid);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mGetUserListener.onGetUser(user, ACTION_CODE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        });
    }

    void updateUser(User user){
        mUsersRef.child(user.getUid()).setValue(user);
    }

    String addActivity(PoolActivity pa){
        String activityId = mActivRef.push().getKey();
        pa.setId(activityId);
        mActivRef.child(activityId).setValue(pa);
        return activityId;
    }

    void getActivity(String aid, final int ACTION_CODE){
        DatabaseReference mAcRef = mActivRef.child(aid);
        mAcRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PoolActivity pa = dataSnapshot.getValue(PoolActivity.class);
                mGetActivityListener.onGetActivity(pa, ACTION_CODE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadActivity:onCancelled", databaseError.toException());
            }
        });
    }

    void updateActivity(PoolActivity pa){
        mActivRef.child(pa.getId()).setValue(pa);
    }

}
