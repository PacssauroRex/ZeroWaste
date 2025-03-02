package com.zerowaste.models.product;

public enum ProductCategory {
    DAIRY("DAIRY"),
    FRUIT("FRUIT"),
    HYGIENE("HYGIENE"),
    CLEANING("CLEANING"),
    DRINK("DRINK"),
    MEAT("MEAT"),
    BAKERY("BAKERY");

    private final String category;

    ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
