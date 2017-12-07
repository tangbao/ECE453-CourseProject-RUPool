package edu.rutgers.ece453.rupool;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";

    private EditText mEditTextEmail;
    private EditText mEditTextPassword;

    private Button mButtonLogin;
    private Button mButtonSignUp;

    private FirebaseAuth mFirebaseAuth;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mEditTextEmail = findViewById(R.id.EditText_Email_LoginActivity);
        mEditTextPassword = findViewById(R.id.EditText_Password_LoginActivity);
        mButtonLogin = findViewById(R.id.Button_Login_LoginActivity);
        mButtonSignUp = findViewById(R.id.Button_SignUp_LoginActivity);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(mEditTextEmail.getText().toString(), mEditTextPassword.getText().toString());
            }
        });
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // init auth
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    private void login(String email, String password) {
        Log.d(TAG, "login: login " + email + password);
        if (!validateForm())
            return;

        showProgressDialog();

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login Success.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private boolean validateForm() {
        return true;
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
