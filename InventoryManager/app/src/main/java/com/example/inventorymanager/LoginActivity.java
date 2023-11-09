package com.example.inventorymanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * On app start, the user is met with the Login Activity. A user can either sign up and create a new account
 * or sign in to an existing account. Once one of these actions are complete, they will be sent to the
 * home screen with all items
 * @author Tomasz Ayobahan
 */
    // Based on code from the Google Firebase snippets-android GitHub page ofr Authentication,
    // License: Apache License, Version 2.0
    // Published March 2021, accessed November 2023
    // https://github.com/firebase/snippets-android/blob/68c9ee528c3f3cc99fcf771edc2da2c642f6115f/auth/app/src/main/java/com/google/firebase/quickstart/auth/EmailPasswordActivity.java
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "UsernamePassword";
    private FirebaseAuth mAuth;
    EditText enterUsername, enterPassword;
    Button login, signup;

    /**
     * Sets up the LoginActivity when it is called when the app is launched.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        // bind ui elements to variables to read text and set error messages
        enterUsername = (EditText) findViewById(R.id.username);
        enterPassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = enterUsername.getText().toString();
                String password = enterPassword.getText().toString();

                // check that the user name is not empty
                if (username.equals("")) {
                    enterUsername.setError("This field is required");
                }
                // check that the password is not empty
                if (password.equals("")) {
                    enterPassword.setError("This field is required");
                }

                // attempt to sign in using provided information
                if (!username.equals("") && !password.equals("")) {
                    String signInEmail = username + "@inventorymanager.com";
                    signIn(signInEmail, password);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = enterUsername.getText().toString();
                String password = enterPassword.getText().toString();

                // check that the user name is not empty
                if (username.equals("")) {
                    enterUsername.setError("This field is required");
                }
                // check that the password is not empty and meets length requirements
                if (password.equals("")) {
                    enterPassword.setError("This field is required");
                } else if (password.length() < 6) {
                    enterPassword.setError("Password must have at least 6 characters");
                }

                // create account if all conditions are met
                if (!username.equals("") && !password.equals("") && password.length() >= 6) {
                    // use dummy email to sign up, user does not need a real email
                    String signUpEmail = username + "@inventorymanager.com";
                    createAccount(signUpEmail, password);
                }
            }
        });
    }

    /**
     * Uses Firebase Authentication method to sign up a new user. The email is the username that is
     * disguised as an email in order for it to be accepted into Firebase Authentication. As emails
     * enforce uniqueness, therefore usernames must be unique, too. Passwords are handled by Authentication
     * for each user.
     * @param email string that holds the modified username to be stored by Authentication
     * @param password string password handled by Authentication
     */
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            enterUsername.setError("Username has been taken");
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Uses Firebase Authentication method to sign in existing user. The email is the username that is
     * disguised as an email in order for it to be accepted into Firebase Authentication.
     * Passwords are handled by Authentication for each user.
     * @param email string that holds the modified username to be handled by Authentication
     * @param password string password handled by Authentication
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            enterUsername.setError("Incorrect username or password");
                            enterPassword.setError("Incorrect username or password");
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * Goes to MainActivity with item data of new or returning.
     * @param user Firebase user to be signed into app
     */
    private void updateUI(FirebaseUser user) {
        // go to main activity
        if (user != null)  {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
