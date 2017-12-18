package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
//        EventFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        PreferenceFragment.OnFragmentInteractionListener,
        NewEventFragment.OnFragmentInteractionListener {


    private static final int REQUESTCODE_LOGIN = 541;
    // start by tb
    private static final String TAG = "Main Activity";
    DatabaseUtils du;
    // end by tb

    FloatingActionButton fab;
    // start by zhu
    private FirebaseAuth mFirebaseAuth;
    private TextView mTextViewUserNameNavHeader;
    private TextView mTextViewEmailNavHeader;
    private DatabaseUtils databaseUtils;
    private List<PoolActivity> allActivityList;
    private MainFragment mainFragment;
    private ShareActionProvider mShareActionProvider;
    private Intent sendIntent;
    // end by zhu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseUtils = new DatabaseUtils();

        // start by sun
        // for sharing
        sendIntent=new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT,"Try RUPool! It's awesome!");
        sendIntent.putExtra(Intent.EXTRA_TEXT,"This app is awesome! Just have a try! download the RUPool right now!");
        sendIntent.setType("text/plain");


        // end by sun

        // start by zhu
        mFirebaseAuth = FirebaseAuth.getInstance();
        // end by zhu

        // start by tb
        du = new DatabaseUtils();
        // end by tb

        ArrayList<String> searchContent = new ArrayList<>();
        searchContent.addAll(Arrays.asList(getResources().getStringArray(R.array.eventList)));

        MainFragment mainFragment = MainFragment.newInstance();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mainFragment, "MainFragment");
        fragmentTransaction.commit();


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                NewEventFragment newEventFragmentventFragment = new NewEventFragment();
                getSupportFragmentManager().popBackStack();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, newEventFragmentventFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        mTextViewUserNameNavHeader = view.findViewById(R.id.TextView_UserName_NavHeaderMain);
        mTextViewEmailNavHeader = view.findViewById(R.id.TextView_Email_NavHeaderMain);
        // start zhu
        // set username in nav header


        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        View childView = autocompleteFragment.getView();
        ImageView searchIcon = childView.findViewById(R.id.place_autocomplete_search_button);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_black_24px));

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
//
//        // 进入程序，显示已有activity
//        databaseUtils.findAllActivity(new Interface.OnFindAllActivityListener() {
//            @Override
//            public void onFindAllActivity(List<PoolActivity> lpa, int RESULT_CODE) {
//                // 得到所有activity列表
//                    allActivityList = lpa;
//                    Toast.makeText(getApplicationContext(),lpa.get(1).getDate().toString(),Toast.LENGTH_SHORT).show();
//
//            }
//        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO:Get info about the selected place.

                Log.i("AUTO", "Place: " + place.getName());
                DatabaseUtils databaseUtils = new DatabaseUtils();
                databaseUtils.findActivityByLocation(place, 123, new Interface.OnFindActivityByPlaceListener() {
                    @Override
                    public void onFindActivityByPlace(List<PoolActivity> lpa, int ACTION_CODE, int RESULT_CODE) {
                        MainFragment mainFragment1 = (MainFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
                        mainFragment1.updateRecyclerView(lpa);
                    }
                });
            }

            @Override
            public void onError(Status status) {
                // TODO:Handle the error.
                Log.i("AUTO", "An error occurred: " + status);
            }
        });
//
//        mainFragment = new MainFragment();
//        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container, mainFragment);
//        fragmentTransaction.commit();


    }

    // start by zhu

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAuth.getCurrentUser() == null
                || !mFirebaseAuth.getCurrentUser().isEmailVerified())
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                    REQUESTCODE_LOGIN);
        if (mFirebaseAuth.getCurrentUser() != null) {
            DatabaseUtils databaseUtils = new DatabaseUtils();
            databaseUtils.getUser(mFirebaseAuth.getCurrentUser().getUid(), 3, new Interface.OnGetUserListener() {
                @Override
                public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                    if (RESULT_CODE == Constant.GET_USER_SUCCESS) {
                        mTextViewUserNameNavHeader.setText(user.getName());
                        mTextViewEmailNavHeader.setText(user.getEmail());
                    } else {
                        startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                    }
                }
            });

        }


    //end by zhu





//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_logout: {
                mFirebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.My_Event: {
                startActivity(new Intent(this,MyEventActivity.class));
                break;
            }


            case R.id.Nav_Logout_MainActivity: {
                mFirebaseAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
            }

            case R.id.Nav_Profile_MainActivity: {
                startActivity(new Intent(this, ShowProfileActivity.class));
                break;


            }

            case R.id.nav_share:{
                startActivity(Intent.createChooser(sendIntent, "TEST HERE"));

            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void myIcon(View view) {
        Toast.makeText(this, "Pop a dialog for users to change his Icon", Toast.LENGTH_SHORT).show();
    }

    public void myProfile(View view) {
        Toast.makeText(this, "Pop a dialog for users to change his Email Address", Toast.LENGTH_SHORT).show();
    }

    public FloatingActionButton getFab() {
        return fab;
    }

    public List<PoolActivity> getAllActivityList() {
        return allActivityList;
    }


    @Override
    public void startEventActivity(PoolActivity poolActivity) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(EventActivity.ARGS_POOLACTIVITY, poolActivity);
        startActivity(intent);
    }

}


