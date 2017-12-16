package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
        findViewById(R.id.Button_ChangeAccount_WaitingEmailVerifyActivity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseAuth.signOut();
                        finish();
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
}
