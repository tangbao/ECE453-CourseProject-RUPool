package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShowProfileActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private TextView mTextViewName;
    private TextView mTextViewGender;
    private TextView mTextViewEmail;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();


        mTextViewName = findViewById(R.id.TextView_Name_ShowProfileActivity);
        mTextViewGender = findViewById(R.id.TextView_Gender_ShowProfileActivity);
        mTextViewEmail = findViewById(R.id.TextView_Email_ShowProfileActivity);

        mRecyclerView = findViewById(R.id.RecyclerView_ShowProfileActivity);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.getCurrentUser().reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                final DatabaseUtils databaseUtils = new DatabaseUtils();
                                databaseUtils.getUser(mFirebaseAuth.getCurrentUser().getUid(), 0, new Interface.OnGetUserListener() {
                                    @Override
                                    public void onGetUser(final User user, int ACTION_CODE, int RESULT_CODE) {
                                        if (RESULT_CODE == Constant.GET_USER_SUCCESS) {
                                            databaseUtils.findAllActivity(new Interface.OnFindAllActivityListener() {
                                                @Override
                                                public void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE) {
                                                    Map<String, PoolActivity> stringPoolActivityMap = new HashMap<>();
                                                    for (PoolActivity poolActivity : lpa)
                                                        stringPoolActivityMap.put(poolActivity.getId(), poolActivity);
                                                    List<PoolActivity> toShow = new LinkedList<>();
//                                                    if (!user.getActivities().isEmpty())
                                                    for (String activityID : user.getActivities())
                                                        toShow.add(stringPoolActivityMap.get(activityID));
                                                    mTextViewName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
                                                    mTextViewGender.setText(user.getGender());
                                                    mTextViewEmail.setText(mFirebaseAuth.getCurrentUser().getEmail());
                                                    mAdapter = new AdapterRecyclerViewMainFragment(toShow, new AdapterRecyclerViewMainFragment.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(PoolActivity poolActivity) {
                                                            Intent intent = new Intent(ShowProfileActivity.this, EventActivity.class);
                                                            intent.putExtra(EventActivity.ARGS_POOLACTIVITY, poolActivity);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    mRecyclerView.setAdapter(mAdapter);
                                                }
                                            });
                                        } else
                                            startActivity(new Intent(ShowProfileActivity.this, EditProfileActivity.class));
                                    }
                                });
                            } else finish();
                        }
                    });
        } else finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_activity_show_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuItem_Edit_OptionMenu_ShowProfileActivity: {
                startActivity(new Intent(this, EditProfileActivity.class));
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
