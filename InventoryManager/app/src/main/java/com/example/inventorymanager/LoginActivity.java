package com.example.inventorymanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "UsernamePassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    EditText enterUsername, enterPassword;
    Button login, signup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        setContentView(R.layout.activity_login);

        enterUsername = (EditText) findViewById(R.id.username);
        enterPassword = (EditText) findViewById(R.id.password);
//        enterEmail = (EditText) findViewById(R.id.editTextTextEmailAddress);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

        String username = enterUsername.getText().toString();
//        String email = enterEmail.getText().toString();
        String password = enterPassword.getText().toString();

        // get all users
        CollectionReference allUsers = db.collection("yourCollection");


        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // username is in
                if (username != "" && password != "") {
                    String signInEmail = username + "@inventorymanager.com";
                    signIn(signInEmail, password);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if any field is false, make text saying cannot proceed
                // check if username is already in the database
                if (username != "" && password != "") {
                    String signUpEmail = username + "@inventorymanager.com";
                    createAccount(signUpEmail, password);
                }
            }
        });

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);


    }
    // [END on_start_check_user]

    // [END on_start_check_user]

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
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
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void updateUI(FirebaseUser user) {
        // go to main activity
        if (user != null)  {
            // extra
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            int index = user.getEmail().indexOf('@');
            String myUser = user.getEmail().substring(0,index);
            intent.putExtra("user", myUser);
            finish();
        }
    }
}
