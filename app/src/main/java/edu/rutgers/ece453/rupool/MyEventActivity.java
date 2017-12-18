package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyEventActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_event);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mRecyclerView = findViewById(R.id.RecyclerView_MyEventActivity);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final DatabaseUtils databaseUtils = new DatabaseUtils();
        databaseUtils.getUser(mFirebaseAuth.getCurrentUser().getUid(), 1, new Interface.OnGetUserListener() {
            @Override
            public void onGetUser(final User user, int ACTION_CODE, int RESULT_CODE) {
                if (RESULT_CODE == Constant.GET_USER_SUCCESS) {
                    databaseUtils.findAllActivity(new Interface.OnFindAllActivityListener() {
                        @Override
                        public void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE) {
                            Map<String, PoolActivity> stringPoolActivityMap = new HashMap<>();
                            for (PoolActivity poolActivity : lpa)
                                stringPoolActivityMap.put(poolActivity.getId(), poolActivity);

                            List<PoolActivity> poolActivities = new LinkedList<>();

                            for (String s : user.getActivities())
                                poolActivities.add(stringPoolActivityMap.get(s));

                            mAdapter = new AdapterRecyclerViewMainFragment(poolActivities, new AdapterRecyclerViewMainFragment.OnItemClickListener() {
                                @Override
                                public void onItemClick(PoolActivity poolActivity) {
                                    Intent intent = new Intent(MyEventActivity.this, EventActivity.class);
                                    intent.putExtra(EventActivity.ARGS_POOLACTIVITY, poolActivity);
                                    startActivity(intent);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    });
                } else finish();
            }
        });


    }
}
