package com.example.asus.trendhimapp.weeklyLookPage.singleWeeklyLookPage;

public class LookProduct {

    private String key, category;

    LookProduct(){};

    LookProduct(String key, String category) {
        this.key = key;
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public String getCategory() {
        return category;
    }

}