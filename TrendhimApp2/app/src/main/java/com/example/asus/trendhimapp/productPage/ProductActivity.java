package com.example.asus.trendhimapp.ProductPage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.trendhimapp.MainActivities.BaseActivity;
import com.example.asus.trendhimapp.WishlistPage.WishlistProduct;
import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.Util.BitmapFlyweight;
import com.example.asus.trendhimapp.Util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProductActivity extends BaseActivity implements View.OnClickListener {
    private ImageView bannerImageView, leftImageView, rightImageView;
    private TextView brandTextView, priceTextView, productNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_product, null, false);
        BaseActivity.drawer.addView(contentView, 0);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        initializeComponents();
        getProductFromIntent();
    }

    private void initializeComponents() {
        bannerImageView = findViewById(R.id.bannerImageView);
        leftImageView = findViewById(R.id.leftImageView);
        rightImageView = findViewById(R.id.rightImageView);

        Button addToCartButton = findViewById(R.id.addToCartButton);
        Button addToWishlistButton = findViewById(R.id.addToWishlistButton);
        addToCartButton.setOnClickListener(this);
        addToWishlistButton.setOnClickListener(this);

        brandTextView = findViewById(R.id.brandTextView);
        brandTextView.setTypeface(null, Typeface.BOLD); // make the text bold
        brandTextView.setTextSize(getResources().getDimension(R.dimen.product_activity_text_size)); // font size: 7sp

        priceTextView = findViewById(R.id.priceTextView);
        priceTextView.setTypeface(null, Typeface.BOLD); // make the text bold
        priceTextView.setTextSize(getResources().getDimension(R.dimen.product_activity_text_size)); // font size: 7sp

        productNameTextView = findViewById(R.id.productNameTextView);
    }

    @SuppressLint("SetTextI18n")
    public void getProductFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("productName");
            String price = intent.getStringExtra("price");
            String bannerPictureUrl = intent.getStringExtra("bannerPictureUrl");
            String leftPictureUrl = intent.getStringExtra("leftPictureUrl");
            String rightPictureUrl = intent.getStringExtra("rightPictureUrl");
            String brand = intent.getStringExtra("brand");
            if (productName != null && price != null && bannerPictureUrl != null && leftPictureUrl != null
                    && rightPictureUrl != null && brand != null) {
                // set all the values & pictures
                brandTextView.setText(brand);
                priceTextView.setText(price + " €");
                productNameTextView.setText(productName);
                BitmapFlyweight.getPicture(
                        bannerPictureUrl, bannerImageView);
                BitmapFlyweight.getPicture(
                        leftPictureUrl, leftImageView);
                BitmapFlyweight.getPicture(
                        rightPictureUrl, rightImageView);
            }
        }
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            if (id == R.id.addToWishlistButton) {
                addToWishlist();
            } else {
                Toast.makeText(this, "Test add to cart", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You must be logged in to access" +
                    " these features!", Toast.LENGTH_LONG).show();
        }
    }

    private void addToWishlist() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(Constants.TABLE_NAME_WISHLIST);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        if (intent != null) {
            String userEmail = user.getEmail();
            String productKey = intent.getStringExtra("productKey");
            String entityName;
            if (productKey.startsWith("watch")) {
                entityName = productKey.replace("\\d", "es");
            } else {
                entityName = productKey.replace("\\d", "s");
            }
            myRef.push().setValue(new WishlistProduct(productKey, entityName, userEmail));
            Toast.makeText(this, "Item successfuly added to wishlist!", Toast.LENGTH_SHORT).show();
        }
    }

}