package com.example.asus.trendhimapp.mainActivities.newProducts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.trendhimapp.R;
import com.example.asus.trendhimapp.categoryPage.CategoryProduct;
import com.example.asus.trendhimapp.mainActivities.recentProducts.RecentProductsAdapter;
import com.example.asus.trendhimapp.productPage.Product;
import com.example.asus.trendhimapp.productPage.ProductActivity;
import com.example.asus.trendhimapp.util.BitmapFlyweight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

public class NewProductsAdapter extends RecyclerView.Adapter<NewProductsAdapter.ViewHolder> {

    private Context context;
    private List<CategoryProduct> newProducts;

    public NewProductsAdapter(Context context, List<CategoryProduct> newProducts) {
        this.newProducts = newProducts;
        this.context = context;
    }

    @Override
    public NewProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View newProductsView = inflater.inflate(R.layout.item_product, parent, false);

        // Return a new holder instance
        return new ViewHolder(newProductsView);
    }

    @Override
    public void onBindViewHolder(NewProductsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final CategoryProduct product = newProducts.get(position);
        // Set item views based on the views and data model
        TextView productName = viewHolder.productNameTextView;
        productName.setText(product.getName());

        TextView productBrand = viewHolder.productBrandTextView;
        productBrand.setText(product.getBrand());

        TextView productPrice = viewHolder.productPriceTextView;
        productPrice.setText(String.valueOf(product.getPrice()));

        ImageView productImage = viewHolder.productImage;
        BitmapFlyweight.getPicture(product.getBannerPictureURL(), productImage);

        /*
        Handle touch event - Redirect to the selected product
        */
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProduct(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newProducts.size();
    }

    /**
     * Populate the recycler view. Get data from the database which name is equal to the parameter.
     */
    public void addData() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("recommended_products");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot product : dataSnapshot.getChildren()) {
                        String productKey = product.getKey();
                        Product p = product.getValue(Product.class);
                        newProducts.add(new CategoryProduct(
                                p.getProductName(), p.getPrice(), p.getBrand(), p.getBannerPictureUrl(), productKey));
                        // Notify the adapter that an item was inserted in position = getItemCount()
                        notifyItemInserted(getItemCount());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    /**
     * Redirect the user to the correct Product
     * @param product
     */
    private void getProduct(final CategoryProduct product) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("recommended_product");
        //get product which key is equal to the one clicked
        databaseReference.orderByChild("key").equalTo(product.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                //Found product
                                final NewProduct newProduct = dataSnapshot1.getValue(NewProduct.class);

                                //Query to the product category to get the product information
                                DatabaseReference category_products_database =
                                        FirebaseDatabase.getInstance().getReference(newProduct.getCategory());

                                category_products_database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                //If the object key is correct
                                                if(Objects.equals(product.getKey(), dataSnapshot1.getKey())) {

                                                    Product foundProduct = dataSnapshot1.getValue(Product.class);
                                                    Intent intent = new Intent(context, ProductActivity.class);

                                                    intent.putExtra("productKey", product.getKey());
                                                    intent.putExtra("productName", foundProduct.getProductName());
                                                    intent.putExtra("brand", foundProduct.getBrand());
                                                    intent.putExtra("bannerPictureUrl", foundProduct.getBannerPictureUrl());
                                                    intent.putExtra("price", String.valueOf(foundProduct.getPrice()));
                                                    intent.putExtra("leftPictureUrl", foundProduct.getLeftPictureUrl());
                                                    intent.putExtra("rightPictureUrl", foundProduct.getRightPictureUrl());

                                                    context.startActivity(intent);

                                                }
                                            }

                                           RecentProductsAdapter.addToRecent(product, newProduct.getCategory());

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