package com.directoryapp.screens.places;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directoryapp.adapters.SliderAdapter;
import com.directoryapp.databinding.ActivitySinglePlaceBinding;
import com.directoryapp.helpers.AuthHelper;
import com.directoryapp.models.Business;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.network.requests.business.GetSingleBusinessRequest;

import java.util.ArrayList;

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
        try {
            RequestDispatcher.getInstance(SinglePlaceActivity.this)
                    .setCallbacks(
                            SinglePlaceActivity.this,
                            this.getClass().getMethod("onFetchBusinessResponse", Object.class),
                            this.getClass().getMethod("onFetchBusinessError", Exception.class)
                    )
                    .dispatch(new GetSingleBusinessRequest(business_id));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void updateBusinessUi(){
        if(currentBusiness == null) return;

        // Set business info to text views
        binding.businessName.setText(currentBusiness.getName());
        binding.address.setText(currentBusiness.getAddress() + ", " + currentBusiness.getTown());
        binding.category.setText(currentBusiness.getCategory().getName());
        binding.description.setText(currentBusiness.getDescription());

        // Images
        loadImages();

        // Open status
        binding.openNow.setVisibility(View.GONE);
        binding.closed.setVisibility(View.GONE);

        if(currentBusiness.isOpen()){
            binding.openNow.setVisibility(View.VISIBLE);
        }else{
            binding.closed.setVisibility(View.VISIBLE);
        }

        // Listeners
        binding.openChat.setOnClickListener(view -> {

        });

        binding.saveBusiness.setOnClickListener(this::toggleSaveBusiness);

        // Load the map
        loadMap();
    }

    private void loadImages(){
        ArrayList<View> views = new ArrayList<>();

        for(String imageUrl:currentBusiness.getAllImages()){
            ImageView img = new ImageView(SinglePlaceActivity.this);
            Toast.makeText(this, imageUrl, Toast.LENGTH_SHORT).show();

            Glide.with(this)
                    .load(imageUrl)
                    .into(img);

            img.setAdjustViewBounds(true);
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            views.add(img);
        }

        SliderAdapter adapter = new SliderAdapter(SinglePlaceActivity.this, views);
        binding.slider.setAdapter(adapter);

    }

    private void loadMap(){

    }

    private void toggleSaveBusiness(View view){
        if(!AuthHelper.isLoggedIn(SinglePlaceActivity.this)){
            Toast.makeText(this, "Log In", Toast.LENGTH_SHORT).show();
        }
    }

    public void onFetchBusinessResponse(Object business){
        if(business == null){
            binding.loader.setVisibility(View.GONE);
            binding.noData.setVisibility(View.VISIBLE);
        }else {
            currentBusiness = (Business) business;

            binding.loader.setVisibility(View.GONE);
            binding.content.setVisibility(View.VISIBLE);

            updateBusinessUi();
        }
    }

    public void onFetchBusinessError(Exception exception){
        binding.loader.setVisibility(View.GONE);
        binding.errorView.setVisibility(View.VISIBLE);
    }
}