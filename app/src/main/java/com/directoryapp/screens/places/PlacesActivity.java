package com.directoryapp.screens.places;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.directoryapp.R;
import com.directoryapp.adapters.AllBusinessesAdapter;
import com.directoryapp.databinding.ActivityPlacesBinding;
import com.directoryapp.models.Business;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.network.requests.business.GetAllBusinessesRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlacesActivity extends AppCompatActivity {

    ActivityPlacesBinding binding;

    // Filters
    Map<String, Object> filters;


    // Will hold the results
    ArrayList<Business> businesses = new ArrayList<>();
    AllBusinessesAdapter businessesAdapter;


    // The request to fetch places, we need to keep an instance to handle pagination
    GetAllBusinessesRequest request;

    int LIMIT = 15;
    int CURRENT_PAGE = 1;

    // These will keep track of the current state views being used
    // e.g when places are being loaded for first time, the main loader is used
    // but not when loading more places on subsequent pages

    View loader = null, content = null, no_data = null, errorView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlacesBinding.inflate(getLayoutInflater());

        filters = new HashMap<>();

        // Add adapter to recycler view
        businessesAdapter = new AllBusinessesAdapter(this, businesses);
        binding.resultsRecycler.setAdapter(businessesAdapter);

        // Get intent data
        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().getString("category_id", null) != null){
                if(getIntent().getExtras().getString("category_name", null) != null){
                    // Set the category as title
                    binding.toolbar.setTitle(getIntent().getExtras().getString("category_name"));
                }

                // Add category id to filters
                filters.put("category_id", getIntent().getExtras().getString("category_id"));
            }

            if(getIntent().getExtras().getString("keyword", null) != null ||
                    getIntent().getExtras().getString("location", null) != null){

                // Set title
                binding.toolbar.setTitle("Search Results");

                if(getIntent().getExtras().getString("keyword", null) != null){
                    // Add to filters
                    filters.put("keyword", getIntent().getExtras().getString("keyword"));
                }

                if(getIntent().getExtras().getString("location", null) != null){
                    // Add to filters
                    filters.put("location", getIntent().getExtras().getString("location"));
                }
            }
        }

        // End activity when user presses back arrow on toolbar
        binding.toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

        // Refreshing when there is an error or no data
        binding.errorView.findViewById(R.id.retry).setOnClickListener(view -> {
            loadPlaces();
        });

        binding.noData.findViewById(R.id.refresh).setOnClickListener(view -> {
            loadPlaces();
        });


        // Request
        request = new GetAllBusinessesRequest(filters, CURRENT_PAGE, LIMIT);

        // Load places
        // Here, we use the main layout state views for loader, error and no data states
        no_data = binding.noData;
        errorView = binding.errorView;
        content = binding.content;
        loader = binding.loader;

        loadPlaces();

        setContentView(binding.getRoot());
    }

    private void loadPlaces(){
        // Show the loader
        if(loader != null) loader.setVisibility(View.VISIBLE);

        // Hide other state views
        if(content != null) content.setVisibility(View.GONE);
        if(no_data != null) no_data.setVisibility(View.GONE);
        if(errorView != null) errorView.setVisibility(View.GONE);

        // Fetch for current page
        try {
            RequestDispatcher.getInstance(PlacesActivity.this)
                    .setCallbacks(
                            this,
                            this.getClass().getMethod("placesLoaded", Object.class),
                            this.getClass().getMethod("errorOccurred", Exception.class)
                    )
                    .dispatch(request.setPage(CURRENT_PAGE));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void placesLoaded(Object result){
        ArrayList<Business> results = (ArrayList<Business>) result;

        // Hide the loader
        loader.setVisibility(View.GONE);

        // Check if results were empty
        if(results.size() == 0){
            // Show the no data state
            no_data.setVisibility(View.VISIBLE);
        }else{
            // Result is a list of businesses
            businesses.addAll(results);

            // Update the recycler view adapter
            businessesAdapter.notifyDataSetChanged();

            // Show the content
            content.setVisibility(View.VISIBLE);


            // Pagination
            // Check if there may be more places to be fetched
            if(results.size() == LIMIT){
                // Might be
                // Show the load more button
                binding.loadMoreBtn.setVisibility(View.VISIBLE);

                // Set the load more progress as current loader, no more as errorView and noData view
                loader = binding.loadMoreProgress;
                errorView = binding.noMoreToFetch;
                no_data = binding.noMoreToFetch;

                // This as content, will be hidden when fetching more
                content = binding.loadMoreBtn;

                // Listen to fetch more clicks
                binding.loadMoreBtn.setOnClickListener(view -> {
                    // Go to next page
                    CURRENT_PAGE++;

                    // Load places
                    loadPlaces();
                });
            }else{
                // Less than LIMIT places returned, might be the last batch of available results
                // Display the text 'No more data'
                binding.noMoreToFetch.setVisibility(View.VISIBLE);

                // Hide the load more button
                binding.loadMoreBtn.setVisibility(View.GONE);
            }
        }
    }

    public void errorOccurred(Exception exception){
        // Hide the loader and show the error view
        loader.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
}