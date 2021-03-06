package com.minhsoumay.funstagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.minhsoumay.funstagram.Fragments.HelpFragment;
import com.minhsoumay.funstagram.Fragments.HomeFragment;
import com.minhsoumay.funstagram.Fragments.ProfileFragment;
import com.minhsoumay.funstagram.Fragments.SearchFragment;

/**
 * @author: Minh Duong
 * COURSE: CSC 317 - Spring 2022
 * @description: This file contains the MainActivity of the app Funstagram. It holds the container
 *               for the home, search and profile fragments, while also containing buttons to
 *               navigate to other functionalities of the app.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;

    /**
     * This method is the on create method which sets the content view and
     * displays the content. The navigation buttons will listen to the clicks, and the
     * corresponding functionality of the app will be shown.
     * @param savedInstanceState    The save instance of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        selectorFragment = new HomeFragment();
                        break;
                    case R.id.nav_search:
                        selectorFragment = new SearchFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;
                    case R.id.nav_profile:
                        selectorFragment = new ProfileFragment();
                        break;
                    case R.id.nav_help:
                        selectorFragment = new HelpFragment();
                        break;
                }

                if (selectorFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, selectorFragment);
                    transaction.commit();
                }

                return true;
            }
        });
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileid = intent.getString("publisherId");
            getSharedPreferences("profile", MODE_PRIVATE).edit().putString("profileId", profileid).apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }
}