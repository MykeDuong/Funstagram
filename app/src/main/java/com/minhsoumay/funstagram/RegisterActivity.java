package com.minhsoumay.funstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the RegisterActivity of the app Funstagram, which allows the
 *               user to register for a new user in the platform. The necessary information
 *               includes the full name, the username, the email and a password with more than
 *               6 characters. It will make a connection to the database on Firebase to do such
 *               task.
 */
public class RegisterActivity extends AppCompatActivity {

    Context context;
    Activity activity;

    private EditText username;
    private EditText name;
    private EditText email;
    private EditText password;
    private android.widget.Button register;
    private TextView loginUser;

    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    /**
     * This method is the on create method which sets the content view and
     * displays the content. When the user clicks on the register button, the given information
     * will be used to create the user on Firebase, and the user will be automatically logged into
     * the platform.
     * @param savedInstanceState    The save instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = getApplicationContext();
        activity = this;

        username = findViewById(R.id.username);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registration_confirm);
        loginUser = findViewById(R.id.login_user);

        pd = new ProgressDialog(this);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = username.getText().toString();
                String txtName = name.getText().toString();
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if (TextUtils.isEmpty(txtUsername) ||
                        TextUtils.isEmpty(txtUsername) ||
                        TextUtils.isEmpty(txtUsername) ||
                        TextUtils.isEmpty(txtUsername)) {
                    Toast.makeText(RegisterActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else if (txtPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password too short!", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txtUsername, txtName, txtEmail, txtPassword);
                }
            }
        });

    }

    /**
     * This method is used to register the user on Firebase using given information. It will
     * create the user, and otherwise inform the exception when failed.
     * @param username  The username of the user
     * @param name      The name of the user
     * @param email     The email of the user
     * @param password  The password of the user
     */
    private void registerUser(String username, String name, String email, String password) {

        pd.setMessage("Please wait...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                System.out.println("Creating map...");
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("name", name);
                map.put("email", email);
                map.put("username", username);
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("bio", "");
                map.put("imageURL", "default");

                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map);
                Toast.makeText(activity, "Update the profile for better experience", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                pd.dismiss();
            }
        }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}