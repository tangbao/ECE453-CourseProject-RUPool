package edu.rutgers.ece453.rupool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ShowProfileActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

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
        mFirebaseUser = mFirebaseAuth.getCurrentUser();


        mTextViewName = findViewById(R.id.TextView_Name_ShowProfileActivity);
        mTextViewGender = findViewById(R.id.TextView_Gender_ShowProfileActivity);
        mTextViewEmail = findViewById(R.id.TextView_Email_ShowProfileActivity);

        mRecyclerView = findViewById(R.id.RecyclerView_ShowProfileActivity);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


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
