package com.sofka.dynamicloading.models;

import com.sofka.dynamicloading.ui.DynamicLoadingPage;

public enum DynamicLoadingExample {

    EXAMPLE_1("/1"),
    EXAMPLE_2("/2");

    private final String path;

    DynamicLoadingExample(String path) {
        this.path = path;
    }

    public String url() {
        return DynamicLoadingPage.BASE_URL + path;
    }
}
