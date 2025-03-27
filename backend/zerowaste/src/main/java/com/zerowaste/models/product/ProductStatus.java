package com.zerowaste.models.product;

public enum ProductStatus {
    AVALIABLE ("AVALIABLE"),
    DONATED ("DONATED"),
    DISCARDED ("DISCARDED");

    private final String status;

    ProductStatus (String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
