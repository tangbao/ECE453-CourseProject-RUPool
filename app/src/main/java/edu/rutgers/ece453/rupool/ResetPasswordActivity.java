package edu.rutgers.ece453.rupool;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity
        implements ResetPasswordEmailSentDialogFragment.ResetPasswordEmailSentDialogFragmentListener {

    private EditText mEditTextEmail;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mFirebaseAuth = FirebaseAuth.getInstance();

        mEditTextEmail = findViewById(R.id.EditText_Email_ResetPasswordActivity);
        Button mButtonResetPassword = findViewById(R.id.Button_ResetPassword_ResetPasswordActivity);

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.sendPasswordResetEmail(mEditTextEmail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DialogFragment dialogFragment = new ResetPasswordEmailSentDialogFragment();
                                    dialogFragment.show(getSupportFragmentManager(), "ResetPasswordEmailSentDialogFragment");
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public void success() {
        finish();
    }
}
