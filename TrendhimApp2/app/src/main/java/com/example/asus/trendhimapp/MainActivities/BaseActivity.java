package com.example.asus.trendhimapp.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.asus.trendhimapp.CategoryPage.CategoryPageActivity;
import com.example.asus.trendhimapp.Login.LoginActivity;
import com.example.asus.trendhimapp.ProductPage.Constants;
import com.example.asus.trendhimapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity  extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    public static DrawerLayout drawer;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.bracelets:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BRACELETS);
                startActivity(intent);
                break;
            case R.id.bags:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BAGS);
                startActivity(intent);
                break;
            case R.id.watches:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_WATCHES);
                startActivity(intent);
                break;
            case R.id.beardCare:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BEARD_CARE);
                startActivity(intent);
                break;
            case R.id.necklaces:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_NECKLACES);
                startActivity(intent);
                break;
            case R.id.ties:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_TIES);
                startActivity(intent);
                break;
            case R.id.bowTies:
                intent = new Intent(this, CategoryPageActivity.class);
                intent.putExtra("category", Constants.TABLE_NAME_BOW_TIES);
                startActivity(intent);
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Start Main Activity whenever the navigation header is clicked
     * @param view
     */
    public void backToHomePage(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    /**
     * Start Log In Activity whenever the Log In button is clicked
     * and the user is not signed in.
     * If the user is signed in - Display Toast
     * @param view
     */
    public void main_to_login(View view){

        if(!isUserOnline()) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Display Toast if the user is already signed in
     * @return true if the user is signed in - false if not
     */
    public boolean isUserOnline(){
        //Check track of the current user
        FirebaseUser currentUser = auth.getCurrentUser();

        boolean isLoggedIn = false;

        if (currentUser != null) {
            // User already logged in
            Toast.makeText(getApplicationContext(), "You are already logged in", Toast.LENGTH_LONG).show();
            isLoggedIn = true;
        }

        return isLoggedIn;
    }

}