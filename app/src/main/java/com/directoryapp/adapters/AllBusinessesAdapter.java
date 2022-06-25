package com.directoryapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.directoryapp.databinding.ViewBusinessBinding;
import com.directoryapp.models.Business;
import com.directoryapp.screens.places.SinglePlaceActivity;

import java.util.ArrayList;

public class AllBusinessesAdapter extends RecyclerView.Adapter<AllBusinessesAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Business> businesses;

    public AllBusinessesAdapter(Context context, ArrayList<Business> businesses) {
        this.context = context;
        this.businesses = businesses;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewBusinessBinding binding = ViewBusinessBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Business business = businesses.get(position);

        holder.binding.title.setText(business.getName());
        holder.binding.address.setText(business.getAddress() + ", " + business.getTown());
        holder.binding.category.setText(business.getCategory().getName());

        // Load the image
        Glide.with(context)
                .load(business.getImageUrl())
                .centerCrop()
                .into(holder.binding.image);

        // When clicked, open single product page, pass the product id and name
        holder.binding.details.setOnClickListener(view -> {
            Intent intent = new Intent(context, SinglePlaceActivity.class);
            intent.putExtra("id", business.getId());
            intent.putExtra("name", business.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    protected static class CategoryViewHolder extends RecyclerView.ViewHolder{
        ViewBusinessBinding binding;

        public CategoryViewHolder(ViewBusinessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
