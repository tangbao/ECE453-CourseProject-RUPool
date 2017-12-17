package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView mImageView;
    private EditText mEditTextName;
    private RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mImageView = findViewById(R.id.ImageView_EditProfileActivity);
        mEditTextName = findViewById(R.id.EditText_Name_EditProfileActivity);
        mRadioGroup = findViewById(R.id.RadioGroup_EditProfileActivity);


        // TODO read user info


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
                // TODO
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
