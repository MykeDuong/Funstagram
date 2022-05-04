package com.minhsoumay.funstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the LoginActivity of the app Funstagram, which allows the
 *               user to log into the Funstagram platform using the registered information.
 */
public class LoginActivity extends AppCompatActivity {

    Activity activity;

    private EditText email;
    private EditText password;
    private android.widget.Button login;
    private TextView registerUser;

    private FirebaseAuth mAuth;

    /**
     * This method is the on create method which sets the content view and
     * displays the content. When the user clicks on the log in button, the given data will
     * be used to get the user from the Firebase database to log the user into the platform.
     * @param savedInstanceState    The save instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = this;

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_confirm);
        registerUser = findViewById(R.id.register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = email.getText().toString();
                String txtPassword = password.getText().toString();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                    Toast.makeText(LoginActivity.this, "Empty Credentials!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(txtEmail, txtPassword);
                }
            }
        });
    }

    /**
     * This method is used to log the user into Funstagram by using the sign in method given
     * by Firebase API. It will use the email and password strings to do so.
     * @param email         The email of the user
     * @param password      The password of the user.
     */
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(activity, "Update the profile for better experience",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                     Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}