package com.directoryapp.screens.places;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.directoryapp.adapters.CategoriesAdapter;
import com.directoryapp.adapters.GridBusinessesAdapter;
import com.directoryapp.databinding.FragmentBusinessesHomeBinding;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.network.requests.business.GetCategoriesRequest;
import com.directoryapp.network.requests.business.GetRecentBusinessesRequest;
import com.directoryapp.models.Category;
import com.directoryapp.models.Business;

import java.util.ArrayList;

public class BusinessesHome extends Fragment {

    FragmentBusinessesHomeBinding binding;
    boolean CATEGORIES_LOADED = false;
    boolean PRODUCTS_LOADED = false;
    boolean VIEW_CREATED = false;

    // Home data holders
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Business> recentProducts = new ArrayList<>();

    // Adapters
    CategoriesAdapter categoriesAdapter = null;
    GridBusinessesAdapter productsAdapter = null;

    public BusinessesHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBusinessesHomeBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VIEW_CREATED = true;

        // Set up listeners
        initListeners();

        // Init the market
        // Load categories
        if(CATEGORIES_LOADED){
            updateCategoriesUi();
        }else{
            // Load in background
            new InitCategoriesTask(getActivity()).execute();
        }

        // Load recent products
        if(PRODUCTS_LOADED){
            updateProductsUi();
        }else{
            // Load in background
            new InitProductsTask(getActivity()).execute();
        }
    }


    private void updateCategoriesUi(){
        categoriesAdapter = new CategoriesAdapter(getActivity(), categories);
        binding.categoriesRecycler.setAdapter(categoriesAdapter);

        // Hide the loader
        binding.categoriesLoader.setVisibility(View.GONE);
        binding.categoriesRecycler.setVisibility(View.VISIBLE);
    }

    private void updateProductsUi(){
        productsAdapter = new GridBusinessesAdapter(getActivity(), recentProducts);
        binding.recentProductsRecycler.setAdapter(productsAdapter);

        // Hide the loader
        binding.recentProductsLoader.setVisibility(View.GONE);

        // Check if there is data
        binding.recentProductsRecycler.setVisibility(View.GONE);
        binding.recentProductsNoData.setVisibility(View.GONE);

        if(recentProducts.size() > 0){
            binding.recentProductsRecycler.setVisibility(View.VISIBLE);
        }else{
            binding.recentProductsNoData.setVisibility(View.VISIBLE);
        }
    }

    private void initListeners(){
        // onClickListeners etc here
        // On click view all, open products screen
        binding.recentsViewAll.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), PlacesActivity.class));
        });

        binding.search.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PlacesActivity.class);

            if(binding.searchKeywordText.getText().toString().length() > 0){
                intent.putExtra("keyword", binding.searchKeywordText.getText().toString());
            }

            if(binding.searchLocationText.getText().toString().length() > 0){
                intent.putExtra("location", binding.searchLocationText.getText().toString());
            }

            startActivity(intent);
        });
    }


    // Loads categories from db in a background thread
    private class InitCategoriesTask extends AsyncTask<String, String, String>{

        Context context;

        public InitCategoriesTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Dispatch firebase request
            try {
                RequestDispatcher.getInstance(context)
                        .setCallbacks(
                                InitCategoriesTask.this,
                                this.getClass().getMethod("loadedCallback", Object.class),
                                this.getClass().getMethod("errorCallback", Exception.class)
                        )
                        .dispatch(new GetCategoriesRequest());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void loadedCallback(Object result) {
            categories = (ArrayList<Category>) result;

            CATEGORIES_LOADED = true;

            if(VIEW_CREATED){
                updateCategoriesUi();
            }
        }

        public void errorCallback(Exception exception) {
            CATEGORIES_LOADED = true;
            updateCategoriesUi();
        }
    }

    // Loads products from db in a background thread
    private class InitProductsTask extends AsyncTask<String, String, String>{

        Context context;

        public InitProductsTask(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            // Dispatch firebase request
            try {
                RequestDispatcher.getInstance(context)
                        .setCallbacks(
                                InitProductsTask.this,
                                this.getClass().getMethod("loadedCallback", Object.class),
                                this.getClass().getMethod("errorCallback", Exception.class)
                        )
                        .dispatch(new GetRecentBusinessesRequest());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void loadedCallback(Object result) {
            recentProducts = (ArrayList<Business>) result;

            PRODUCTS_LOADED = true;

            if(VIEW_CREATED){
                updateProductsUi();
            }
        }

        public void errorCallback(Exception exception) {
            PRODUCTS_LOADED = true;
            updateProductsUi();
        }
    }
}