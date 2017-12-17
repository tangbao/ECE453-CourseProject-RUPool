package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class WaitingEmailVerifyActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_email_verify);

        mFirebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.Button_Refresh_WaitingEmailVerifyActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFirebaseAuth.getCurrentUser() != null)
                            mFirebaseAuth.getCurrentUser().reload()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (mFirebaseAuth.getCurrentUser().isEmailVerified())
                                                finish();
                                        }
                                    });
                        else
                            finish();
                    }
                });
        findViewById(R.id.Button_ResendEmail_WaitingEmailVerifyActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mFirebaseAuth.getCurrentUser() != null) {
                            mFirebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(WaitingEmailVerifyActivity.this, "Resend verify email success", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else finish();
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseAuth.getCurrentUser().reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                                finish();
                            }
                        }
                    });
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_activity_waiting_email_verify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuItem_LogOut_OptionMenu_WaitingEmailVerifyActivity: {
                mFirebaseAuth.signOut();
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
