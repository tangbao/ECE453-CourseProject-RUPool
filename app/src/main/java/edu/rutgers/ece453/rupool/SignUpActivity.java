package edu.rutgers.ece453.rupool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    public static final int SIGNUP_SUCCESS = 0;
    public static final int SIGNUP_FAILURE = 1;
    public static final int SIGNUP_CANCEL = 3;


    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonSignUp;

    private FirebaseAuth mFirebaseAuth;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditTextEmail = findViewById(R.id.EditText_Email_SignUpActivity);
        mEditTextPassword = findViewById(R.id.EditText_Password_SignUpActivity);
        mButtonSignUp = findViewById(R.id.Button_SignUp_SignUpActivity);

        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(SIGNUP_CANCEL);
        super.onBackPressed();
    }

    private void signUp(String email, String password) {
        showProgressDialog();

        // TODO validate email and password

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: success");
                            setResult(SIGNUP_SUCCESS);
                            finish();
                        } else {
                            Log.w(TAG, "onComplete: failure", task.getException());
                            setResult(SIGNUP_FAILURE);
                            finish();
                        }

                    }
                });

    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
