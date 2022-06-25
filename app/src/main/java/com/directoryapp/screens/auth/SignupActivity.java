package com.directoryapp.screens.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.directoryapp.databinding.ActivitySignupBinding;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.network.requests.auth.SignupRequest;
import com.directoryapp.helpers.AuthHelper;
import com.directoryapp.helpers.Utils;
import com.directoryapp.models.User;
import com.directoryapp.screens.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    // Binding
    ActivitySignupBinding binding;

    // Loader
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());

        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Creating your account...")
                .setCancellable(false)
                .setAnimationSpeed(5)
                .setDimAmount(0.5f);

        // Clicks
        binding.signup.setOnClickListener(view -> {
            signup();
        });

        binding.backToLogin.setOnClickListener(view -> {
            finish();
        });

        // Set view
        setContentView(binding.getRoot());
    }

    private void signup(){
        String name = binding.name.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String phone = binding.phone.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        // Validation
        if(name.length() == 0){
            Snackbar.make(binding.signup, "Enter your name", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(email.length() == 0){
            Snackbar.make(binding.signup, "Enter your email", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(phone.length() == 0){
            Snackbar.make(binding.signup, "Enter your phone number", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(password.length() == 0){
            Snackbar.make(binding.signup, "Enter your password", Snackbar.LENGTH_LONG).show();
            return;
        }

        // Show the progress loader
        hud.show();

        // Create user
        User user = new User(
                name,
                email,
                phone,
                new ArrayList<>()
        );

        // Dispatch request
        try {
            RequestDispatcher
                    .getInstance(this)
                    .setCallbacks(
                            this,
                            this.getClass().getMethod("onSignupSuccess", Object.class),
                            this.getClass().getMethod("onSignupError", Exception.class)
                    )
                    .dispatch(new SignupRequest(user, password));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void onSignupSuccess(Object result){
        // Update auth state
        User user = (User) result;
        AuthHelper.saveUser(SignupActivity.this, user);

        hud.dismiss(); // Hiding the loader

        startActivity(new Intent(
                this,
                MainActivity.class
        ));

        finish();
    }

    public void onSignupError(Exception exception){
        hud.dismiss(); // Hiding the loader
        Utils.alert(SignupActivity.this, "Signup Failed", exception.getMessage());
    }
}