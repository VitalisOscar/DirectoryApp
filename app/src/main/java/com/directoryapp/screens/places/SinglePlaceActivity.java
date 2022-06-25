package com.directoryapp.screens.places;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.directoryapp.databinding.ActivitySinglePlaceBinding;
import com.directoryapp.models.Business;

public class SinglePlaceActivity extends AppCompatActivity {

    ActivitySinglePlaceBinding binding;

    String business_id = null;
    Business currentBusiness = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySinglePlaceBinding.inflate(getLayoutInflater());

        // Get the passed business id
        if(getIntent().getExtras().getString("id", null) != null){
            business_id = getIntent().getExtras().getString("id");
        }

        // If id has not been passed, end activity
        if(business_id == null){
            finish();
        }

        // End activity when user presses back arrow on toolbar
        binding.toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        // Set toolbar title to that of the business
        if(getIntent().getExtras().getString("name", null) != null){
            binding.toolbar.setTitle(getIntent().getExtras().getString("name"));
        }

        // Get the business
        getDetails();

        setContentView(binding.getRoot());
    }

    private void getDetails(){

    }
}