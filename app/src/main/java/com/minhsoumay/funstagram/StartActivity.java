package com.minhsoumay.funstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the StartActivity of the app Funstagram, which allows the
 *               user to choose whether they want to register or log in to get into the app.
 */
public class StartActivity extends AppCompatActivity {

    private ImageView iconImage;
    private LinearLayout linearLayout;
    private android.widget.Button register;
    private android.widget.Button login;

    /**
     * This method is the on create method which sets the content view and
     * displays the content.
     * @param savedInstanceState    The save instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        iconImage = findViewById(R.id.icon_image);
        linearLayout = findViewById(R.id.starting_linear_layout);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);

        linearLayout.animate().alpha(0f).setDuration(1);

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -1000);
        animation.setDuration(1000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        iconImage.setAnimation(animation);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class).
                        addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK) |
                                  Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class).
                        addFlags((Intent.FLAG_ACTIVITY_CLEAR_TASK) |
                                  Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }

    /**
     * This method is used to skip the log in part of the app when the user has already logged
     * in. However, as the log out functionality has yet been implemented, the code in this
     * method is commented out.
     */
    @Override
    protected void onStart() {
        super.onStart();
        /*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
        */
    }

    /**
     * This class implements the AnimationListener class of the Animation. It creates the
     * behavior of the animation when the it ends.
     */
    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);
            linearLayout.animate().alpha(1f).setDuration(1000);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }
}