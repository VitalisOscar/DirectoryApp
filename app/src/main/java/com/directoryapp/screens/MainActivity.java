package com.directoryapp.screens;

import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.directoryapp.R;
import com.directoryapp.databinding.ActivityMainBinding;
import com.directoryapp.network.RequestDispatcher;
import com.directoryapp.network.requests.business.SeedDbRequest;
import com.directoryapp.screens.forum.ForumHome;
import com.directoryapp.screens.places.BusinessesHome;

public class MainActivity extends BaseActivity {

    // View binding
    ActivityMainBinding binding;

    // Fragments
    BusinessesHome businessesHome;
    ForumHome forumHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View bindings
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // Initialize the app shell
        initShell();

        // Set the content's view
        setContentView(binding.getRoot());
    }

    private void initShell(){
        businessesHome = new BusinessesHome();
        forumHome = new ForumHome();

        // Attach
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(binding.frameHome.getId(), businessesHome).commit();
        fm.beginTransaction().replace(binding.frameForum.getId(), forumHome).commit();

        // Listen to bottom nav
        binding.bottomNav.setOnItemSelectedListener(item -> {
            // Hide all
            binding.frameHome.setVisibility(View.GONE);
            binding.frameForum.setVisibility(View.GONE);

            // Set outlined icons
            MenuItem itemMarket = binding.bottomNav.getMenu().getItem(0);
            MenuItem itemForum = binding.bottomNav.getMenu().getItem(1);
            MenuItem itemMore = binding.bottomNav.getMenu().getItem(2);

            itemMarket.setIcon(R.drawable.ic_home);
            itemForum.setIcon(R.drawable.forum_outline);
            itemMore.setIcon(R.drawable.more_outline);

            // For selected item, set appropriate title, filled icon and show associated layout
            if(item.getItemId() == R.id.itemHome){
                binding.frameHome.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("Home");
                itemMarket.setIcon(R.drawable.ic_home_active);
                return true;
            }

            if(item.getItemId() == R.id.itemChats){
                binding.frameForum.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("Chats");
                itemForum.setIcon(R.drawable.forum_filled);
                return true;
            }

            if(item.getItemId() == R.id.itemMore){
                binding.frameMore.setVisibility(View.VISIBLE);
                binding.toolbar.setTitle("More");
                itemMore.setIcon(R.drawable.more_filled);
                return true;
            }

            return false;
        });

        // Select the market destination by default
        binding.bottomNav.setSelectedItemId(R.id.itemHome);


        // Options Menu
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.itemSeed){
                RequestDispatcher.getInstance(MainActivity.this)
                        .dispatch(new SeedDbRequest());

                return true;
            }

            return false;
        });
    }
}