package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private String mStringGender = "Male";
    private User mUser;

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
        DatabaseUtils databaseUtils = new DatabaseUtils();
        databaseUtils.getUser(mFirebaseAuth.getCurrentUser().getUid(), 999, new Interface.OnGetUserListener() {
            @Override
            public void onGetUser(User user, int ACTION_CODE, int RESULT_CODE) {
                if (RESULT_CODE == Constant.GET_USER_SUCCESS) {
                    mUser = user;
                    mEditTextName.setText(mFirebaseAuth.getCurrentUser().getDisplayName());
                    switch (mUser.getGender()) {
                        case "Male": {
                            mRadioGroup.check(R.id.RadioButton_Male_EditProfileActivity);
                            break;
                        }
                        case "Female": {
                            mRadioGroup.check(R.id.RadioButton_Female_EditProfileActivity);
                            break;
                        }
                        case "Other": {
                            mRadioGroup.check(R.id.RadioButton_Other_EditProfileActivity);
                            break;
                        }
                        default: {
                            mRadioGroup.check(R.id.RadioButton_Male_EditProfileActivity);
                            break;
                        }
                    }
                } else {
                    mUser = null;
                }
            }
        });
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

                if (mUser == null) {
                    mUser = new User(
                            mFirebaseAuth.getCurrentUser().getUid(),
                            mStringGender,
                            mFirebaseAuth.getCurrentUser().getEmail(),
                            mEditTextName.getText().toString());
                } else {
                    mUser.setName(mEditTextName.getText().toString());
                    mUser.setGender(mStringGender);
                }
                DatabaseUtils databaseUtils = new DatabaseUtils();
                databaseUtils.updateUser(mUser);
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
