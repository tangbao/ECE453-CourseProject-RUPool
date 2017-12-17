package edu.rutgers.ece453.rupool;

import android.content.Context;
import android.net.Uri;
import android.support.v4.util.Pools;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.rutgers.ece453.rupool.Interface.OnGetActivityListener;
import edu.rutgers.ece453.rupool.Interface.OnGetUserListener;
import edu.rutgers.ece453.rupool.Interface.OnFindActivityByPlaceListener;
import edu.rutgers.ece453.rupool.Interface.OnFindAllActivityListener;

import static edu.rutgers.ece453.rupool.Constant.FIND_ACTIVITY_BY_PLACE_FAIL;
import static edu.rutgers.ece453.rupool.Constant.FIND_ACTIVITY_BY_PLACE_SUCCESS;
import static edu.rutgers.ece453.rupool.Constant.GET_ACTIVITY_FAIL;
import static edu.rutgers.ece453.rupool.Constant.GET_ACTIVITY_SUCCESS;
import static edu.rutgers.ece453.rupool.Constant.GET_USER_FAIL;
import static edu.rutgers.ece453.rupool.Constant.GET_USER_SUCCESS;
import static edu.rutgers.ece453.rupool.Constant.GET_ALL_ACTIVITY_SUCCESS;
import static edu.rutgers.ece453.rupool.Constant.GET_ALL_ACTIVITY_FAIL;

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
    private OnFindActivityByPlaceListener mFindActivityByPlaceListener;
    private OnFindAllActivityListener mFindAllActivityListener;

    private DatabaseReference mDatabase;
    private DatabaseReference mUsersRef;
    private DatabaseReference mActivRef;

    DatabaseUtils(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mDatabase.child("users");
        mActivRef = mDatabase.child("activities");
    }

    //========================User Methods================================

    void addUser(User user){
        mUsersRef.child(user.getUid()).setValue(user);
    }

    void getUser(String uid, final int ACTION_CODE){
        DatabaseReference mUserRef = mUsersRef.child(uid);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                if(dataSnapshot.exists()){
                    user = dataSnapshot.getValue(User.class);
                    mGetUserListener.onGetUser(user, ACTION_CODE, GET_USER_SUCCESS);
                }else{
                    mGetUserListener.onGetUser(user, ACTION_CODE, GET_USER_FAIL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadUser:onCancelled", databaseError.toException());
            }
        });
    }

    void setOnGetUserListener(OnGetUserListener onGetUserListener){
        mGetUserListener = onGetUserListener;
    }

    void updateUser(User user){
        mUsersRef.child(user.getUid()).setValue(user);
    }

    //======================Activity Methods===============================


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
                PoolActivity pa = new PoolActivity();
                if(dataSnapshot.exists()){
                    pa = dataSnapshot.getValue(PoolActivity.class);
                    mGetActivityListener.onGetActivity(pa, ACTION_CODE, GET_ACTIVITY_SUCCESS);
                }else{
                    mGetActivityListener.onGetActivity(pa, ACTION_CODE, GET_ACTIVITY_FAIL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadActivity:onCancelled", databaseError.toException());
            }
        });
    }

    void setOnGetActivityListener(OnGetActivityListener onGetActivityListener){
        mGetActivityListener = onGetActivityListener;
    }

    void updateActivity(PoolActivity pa){
        mActivRef.child(pa.getId()).setValue(pa);
    }

    //==========================Search Activity==========================

    void findActivityByLocation(Place place,final int ACTION_CODE){

        Log.e(TAG,place.getAddress().toString());
        Query query = mActivRef.orderByChild("destiAddress").equalTo(place.getAddress().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<PoolActivity> result = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        result.add(data.getValue(PoolActivity.class));
                    }
                    mFindActivityByPlaceListener.onFindActivityByPlace(result, ACTION_CODE, FIND_ACTIVITY_BY_PLACE_SUCCESS);
                }else {
                    //return fail
                    mFindActivityByPlaceListener.onFindActivityByPlace(result, ACTION_CODE, FIND_ACTIVITY_BY_PLACE_FAIL);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               Log.w(TAG, "findPoolActivityByLocation:onCancelled", databaseError.toException());
            }
        });


    }

    void setOnFindActivityByLocationListener(OnFindActivityByPlaceListener onFindActivityByPlaceListener){
        mFindActivityByPlaceListener = onFindActivityByPlaceListener;
    }

    void findAllActivity(){
        mActivRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(final DataSnapshot dataSnapshot) {
            List<PoolActivity> result = new ArrayList<>();
            if(dataSnapshot!=null){
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    PoolActivity pa = data.getValue(PoolActivity.class);
                    result.add(pa);
                }
                mFindAllActivityListener.onFindAllActivity(result, GET_ALL_ACTIVITY_SUCCESS );
            }else{
                Log.e(TAG, "no avitivity now");
                mFindAllActivityListener.onFindAllActivity(result, GET_ALL_ACTIVITY_FAIL);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "findAllActivities:onCancelled", databaseError.toException());
        }
    });
    }

    void setOnFindAllActivityListener(OnFindAllActivityListener onFindAllActivityListener){
        mFindAllActivityListener = onFindAllActivityListener;
    }

}
