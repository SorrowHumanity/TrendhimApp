package com.example.asus.trendhimapp.MainActivities.RecentProducts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.CategoryPage.CategoryProduct;
import com.example.asus.trendhimapp.ProductPage.ProductActivity;
import com.example.asus.trendhimapp.ProductPage.Products.BitmapFactory;
import com.example.asus.trendhimapp.ProductPage.Products.Product;
import com.example.asus.trendhimapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class RecentProductsAdapter extends RecyclerView.Adapter<RecentProductsAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> recentProducts;

    public RecentProductsAdapter(Context context, List<CategoryProduct> recentProducts) {
        this.recentProducts = recentProducts;
        this.context = context;
    }

    @Override
    public RecentProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recentProductsView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        return new ViewHolder(recentProductsView);
    }

    @Override
    public void onBindViewHolder(RecentProductsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final CategoryProduct product = recentProducts.get(position);

        // Set item views based on the views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(product.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice()));

        ImageView productImage = viewHolder.productImage;
        BitmapFactory.getPicture(product.getBannerPictureURL(), productImage);

        /*
        Handle touch event - Redirect to the selected product
        */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recent_products");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for(final DataSnapshot product : dataSnapshot.getChildren()) {
                                RecentProduct recentProduct = product.getValue(RecentProduct.class);
                                final String productKey = recentProduct.getKey();
                                String productCategory = recentProduct.getCategory();
                                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(productCategory);
                                Query query = myRef.orderByChild(productKey);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot product : dataSnapshot.getChildren()) {
                                                Product foundProduct = product.getValue(Product.class);
                                                if(Objects.equals(product.getKey(), productKey)) {
                                                    Intent intent = new Intent(context, ProductActivity.class);

                                                    intent.putExtra("productName", foundProduct.getProductName());
                                                    intent.putExtra("brand", foundProduct.getBrand());
                                                    intent.putExtra("bannerPictureUrl", foundProduct.getBannerPictureUrl());
                                                    intent.putExtra("price", String.valueOf(foundProduct.getPrice()));
                                                    intent.putExtra("leftPictureUrl", foundProduct.getLeftPictureUrl());
                                                    intent.putExtra("rightPictureUrl", foundProduct.getRightPictureUrl());
                                                    intent.putExtra("ID", "FromRecyclerView");
                                                    context.startActivity(intent);
                                                }

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return recentProducts.size();
    }

    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     */
    public void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("recent_products");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(final DataSnapshot product : dataSnapshot.getChildren()) {
                        RecentProduct recentProduct = product.getValue(RecentProduct.class);
                        final String productKey = recentProduct.getKey();
                        String productCategory = recentProduct.getCategory();
                        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference(productCategory);
                        Query query = myRef.orderByChild(productKey);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot product : dataSnapshot.getChildren()) {
                                        Product p = product.getValue(Product.class);
                                        if(Objects.equals(product.getKey(), productKey)) {
                                            recentProducts.add(new CategoryProduct(
                                                    p.getProductName(), p.getPrice(), p.getBrand(), p.getBannerPictureUrl(), productKey));
                                            // Notify the adapter that an item was inserted in position = getItemCount()
                                            notifyItemInserted(getItemCount());
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    /**
     *  Provide a direct reference to each of the views
     * used to cache the views within the layout for fast access
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productBrandTextView;
        ImageView productImage;

        /**
         *  We also create a constructor that does the view lookups to find each subview
         */
        ViewHolder(View itemView) {
        /*
          Stores the itemView in a public final member variable that can be used
          to access the context from any ViewHolder instance.
         */
            super(itemView);

            productNameTextView = itemView.findViewById(R.id.product_name_main);
            productPriceTextView = itemView.findViewById(R.id.product_price_main);
            productBrandTextView = itemView.findViewById(R.id.brand_name_main);
            productImage = itemView.findViewById(R.id.product_image_main);

        }
    }

}