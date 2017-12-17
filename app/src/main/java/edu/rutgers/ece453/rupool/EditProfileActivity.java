package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private String mStringGender = "Male";

    private ImageView mImageView;
    private EditText mEditTextName;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mImageView = findViewById(R.id.ImageView_EditProfileActivity);
        mEditTextName = findViewById(R.id.EditText_Name_EditProfileActivity);
        mRadioGroup = findViewById(R.id.RadioGroup_EditProfileActivity);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.RadioButton_Male_EditProfileActivity: {
                        mStringGender = "Male";
                        break;
                    }
                    case R.id.RadioButton_Female_EditProfileActivity: {
                        mStringGender = "Female";
                        break;
                    }
                    case R.id.RadioButton_Other_EditProfileActivity: {
                        mStringGender = "Other";
                        break;
                    }
                }
            }
        });

        // TODO read user info


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() == null) finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_activity_edit_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuItem_Save_OptionMenu_EditProfileActivity: {
                UserProfileChangeRequest userProfileChangeRequest
                        = new UserProfileChangeRequest.Builder()
                        .setDisplayName(mEditTextName.getText().toString())
                        .build();
                mFirebaseAuth.getCurrentUser().updateProfile(userProfileChangeRequest);

                User user = new User(mFirebaseAuth.getCurrentUser().getUid(), mStringGender);
                DatabaseUtils databaseUtils = new DatabaseUtils();
                databaseUtils.updateUser(user);
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
