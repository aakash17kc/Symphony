package com.logix.symphony;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Symphony";
    EditText mEmail,mPassword;
    String mGetEmail,mGetPassword;
    Button mLoginButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();



        mLoginButton = findViewById(R.id.emaillogin);

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginButton.setEnabled(true);
                mLoginButton.setEms(10);
                mLoginButton.setTextColor(getResources().getColor(android.R.color.black));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLoginButton.setOnClickListener(this);



    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        mGetEmail = mEmail.getText().toString().trim();
        mGetPassword = mPassword.getText().toString().trim();
        mLoginButton.setText("Logining In...");
        if (isValid(mGetEmail)) {
           // Toast.makeText(LoginActivity.this, "Valid", Toast.LENGTH_SHORT).show();


            mAuth.signInWithEmailAndPassword(mGetEmail, mGetPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Signing In", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SongActivity.class);
                                startActivity(intent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Creating User", Toast.LENGTH_SHORT).show();
                                mCreateUser();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });


            //}
        }
    }

    private void mCreateUser() {

        mAuth.createUserWithEmailAndPassword(mGetEmail, mGetPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,"User Created",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, SongActivity.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
