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
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EventFragment.OnFragmentInteractionListener ,
        MainFragment.OnFragmentInteractionListener,PreferenceFragment.OnFragmentInteractionListener,NewEventFragment.OnFragmentInteractionListener{


    private static final int REQUESTCODE_LOGIN = 541;
    FloatingActionButton fab;
    // start by zhu
    FirebaseAuth mFirebaseAuth;
    // end by zhu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // start by zhu
        mFirebaseAuth = FirebaseAuth.getInstance();
        // end by zhu

        ArrayList<String> searchContent=new ArrayList<>();
        searchContent.addAll(Arrays.asList(getResources().getStringArray(R.array.eventList)));

        MainFragment mainFragment=new MainFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,mainFragment);
        fragmentTransaction.commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                NewEventFragment newEventFragmentventFragment=new NewEventFragment();
                getSupportFragmentManager().popBackStack();
                android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,newEventFragmentventFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // start by zhu

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAuth.getCurrentUser() == null
                || !mFirebaseAuth.getCurrentUser().isEmailVerified())
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class),
                    REQUESTCODE_LOGIN);

    }


    //end by zhu

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem=menu.findItem(R.id.menu_search);
        SearchView searchView=(SearchView)menuItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;

            }
        });

        return super.onCreateOptionsMenu(menu);
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
        int id = item.getItemId();

        if (id == R.id.My_Event) {
            // Handle the camera action
            EventFragment eventFragment=new EventFragment();
            getSupportFragmentManager().popBackStack();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,eventFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        } else if(id==R.id.nav_preference) {
            fab.show();
            PreferenceFragment preferenceFragment=new PreferenceFragment();
            getSupportFragmentManager().popBackStack();
            android.support.v4.app.FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,preferenceFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void myIcon(View view){
        Toast.makeText(this,"Pop a dialog for users to change his Icon",Toast.LENGTH_SHORT).show();
    }

    public void myProfile(View view){
        Toast.makeText(this,"Pop a dialog for users to change his Email Address",Toast.LENGTH_SHORT).show();
    }

}


