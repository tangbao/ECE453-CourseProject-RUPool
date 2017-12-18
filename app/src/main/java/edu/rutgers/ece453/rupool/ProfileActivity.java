package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    public static final String ARGS_USER = "edu.rutgers.ece453.rupool.ProfileActivity.ARGS.USER";

    private User mUser;

    private ImageView mImageView;
    private TextView mTextViewName;
    private TextView mTextViewGender;
    private TextView mTextViewEmail;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTextViewName = findViewById(R.id.TextView_Name_ProfileActivity);
        mTextViewGender = findViewById(R.id.TextView_Gender_ProfileActivity);
        mTextViewEmail = findViewById(R.id.TextView_Email_ProfileActivity);
        mRecyclerView = findViewById(R.id.RecyclerView_ProfileActivity);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();

        if (intent.hasExtra(ARGS_USER)) {
            mUser = (User) intent.getSerializableExtra(ARGS_USER);
            DatabaseUtils databaseUtils = new DatabaseUtils();
            databaseUtils.findAllActivity(new Interface.OnFindAllActivityListener() {
                @Override
                public void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE) {
                    Map<String, PoolActivity> stringPoolActivityMap = new HashMap<>();
                    for (PoolActivity poolActivity : lpa)
                        stringPoolActivityMap.put(poolActivity.getId(), poolActivity);
                    List<PoolActivity> toShow = new LinkedList<>();
//                                                    if (!user.getActivities().isEmpty())
                    for (String activityID : mUser.getActivities())
                        toShow.add(stringPoolActivityMap.get(activityID));
                    mTextViewName.setText(mUser.getName());
                    mTextViewGender.setText(mUser.getGender());
                    mTextViewEmail.setText(mUser.getEmail());
                    mAdapter = new AdapterRecyclerViewMainFragment(toShow, new AdapterRecyclerViewMainFragment.OnItemClickListener() {
                        @Override
                        public void onItemClick(PoolActivity poolActivity) {
                            Intent intent = new Intent(ProfileActivity.this, EventActivity.class);
                            intent.putExtra(EventActivity.ARGS_POOLACTIVITY, poolActivity);
                            startActivity(intent);
                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                }
            });
        } else finish();

    }
}
