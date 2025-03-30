package com.zerowaste.services.broadcasts.errors;

import java.util.List;

public class BroadcastListProductsNotFoundException extends Exception {
    public BroadcastListProductsNotFoundException(List<String> notFoundProducts) {
        super(String.format(
            "Alguns produtos não foram encontrados: %s",
            notFoundProducts
                .stream()
                .reduce((acc, id) -> acc + ", " + id)
                .orElse(""))
        );
    }
}
