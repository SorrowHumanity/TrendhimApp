package com.example.asus.trendhimapp.mainActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.newProducts.NewProductsAdapter;
import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.weeklyLookPage.WeeklyLookActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    public static RecentProductsAdapter adapter;
    public static TextView noRecentProducts;
    private int i = 0, i1= 0; //handle image clicks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.content_main, null, false);
        BaseActivity.drawer.addView(contentView, 0);

        initializeComponents();
        initializeRecentProductRecyclerView();
        initializeRecommendedProductsRecyclerView();
    }

    /**
     * Initialize Main Activity components and handle image clicks listeners
     */
    void initializeComponents() {

        final LinearLayout recentProductsLayout = findViewById(R.id.recentProductsLayout);

        ImageView recentProductImage = findViewById(R.id.recentproductsImage);
        ImageView weeklyLookImage = findViewById(R.id.weeklyLookImage);
        noRecentProducts = findViewById(R.id.noRecentProducts);
        ImageView recommendedProductsImage = findViewById(R.id.recommendedProductsImage);
        final LinearLayout recommendedProductsLayout = findViewById(R.id.recommendedProductsLayout);

        /*
         * Hide or show recent products recycler view on image clicked
         */
        recentProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i % 2 == 0)
                    recentProductsLayout.setVisibility(View.VISIBLE);
                if(i % 2 != 0)
                    recentProductsLayout.setVisibility(View.GONE);
                i++;
            }
        });

        /*
         * Hide or show recommended products recycler view on image clicked
         */
        recommendedProductsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i1 % 2 == 0)
                    recommendedProductsLayout.setVisibility(View.VISIBLE);
                if(i1 % 2 != 0)
                    recommendedProductsLayout.setVisibility(View.GONE);
                i1++;
            }
        });

         /*
         * Open Weekly look Activity on image clicked
         */
        weeklyLookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toWeeklyLook = new Intent(getApplicationContext(), WeeklyLookActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view, "profile");
                startActivity(toWeeklyLook, options.toBundle());
            }
        });

    }
    /**
     * Initialize recent product recycler view
     */
    void initializeRecentProductRecyclerView() {
        // Lookup the recycler view in activity layout
        RecyclerView recyclerView = findViewById(R.id.recyclerViewmain);

        // Initialize categories
        ArrayList<CategoryProduct> recentProducts = new ArrayList<>();

        // Create adapter for the categories
        adapter = new RecentProductsAdapter(this, recentProducts);

        // Attach the adapter to the recycler view
        recyclerView.setAdapter(adapter);

        //Populate the recycler view
        adapter.addData();

        SnapHelper helper = new LinearSnapHelper();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        helper.attachToRecyclerView(recyclerView);
    }

    /**
     * Initialize recommended product recycler view
     */
    void initializeRecommendedProductsRecyclerView() {

        // Lookup the recycler view in the activity layout (new)
        RecyclerView recyclerViewNewProducts = findViewById(R.id.rvRecommendProducts);

        //Recommended products initialize
        ArrayList<CategoryProduct> recommendedProducts = new ArrayList<>();

        // Create a new adapter for the category
        NewProductsAdapter newProductAdapter = new NewProductsAdapter(this, recommendedProducts);

        // Attach the adapter to the recycler view
        recyclerViewNewProducts.setAdapter(newProductAdapter);

        //Populate the recycler view
        newProductAdapter.addData();

        SnapHelper helperNew = new LinearSnapHelper();
        LinearLayoutManager layoutManagerNew = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNewProducts.setLayoutManager(layoutManagerNew);

        helperNew.attachToRecyclerView(recyclerViewNewProducts);
    }

}

