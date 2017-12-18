package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class EventActivity extends AppCompatActivity
        implements EventFragment.OnFragmentInteractionListener {
    public static final String ARGS_POOLACTIVITY = "edu.rutgers.ece453.rupool.EventActivity.ARGS.POOLACTIVITY";

    private PoolActivity mPoolActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();

        if (intent.hasExtra(ARGS_POOLACTIVITY)) {
            mPoolActivity = (PoolActivity) intent.getSerializableExtra(ARGS_POOLACTIVITY);
        } else finish();


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        EventFragment eventFragment = EventFragment.newInstance(mPoolActivity);
        fragmentTransaction.add(R.id.FrameLayout_EventActivity, eventFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void startProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARGS_USER, user);
        startActivity(intent);
    }
}
