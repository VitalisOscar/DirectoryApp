package com.directoryapp.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.directoryapp.helpers.AuthHelper;
import com.directoryapp.screens.auth.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if a user is not logged in and redirect lo login page
        if(!AuthHelper.isLoggedIn(this)){
            startActivity(new Intent(
                this,
                LoginActivity.class
            ));

            // End activity
            finish();
        }

    }

}
