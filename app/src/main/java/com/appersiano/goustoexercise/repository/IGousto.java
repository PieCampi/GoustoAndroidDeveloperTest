package com.appersiano.goustoexercise.repository;

import android.support.annotation.NonNull;

import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;

import java.util.List;

public interface IGousto {

    void getCategoryList(@NonNull final CategoriesCallback callback);

    void getProductWithInfo(@NonNull Integer imageSize, @NonNull final ProductsCallback callback);

    interface CategoriesCallback {
        void onSuccess(List<Category> categoryList);

        void onFailure(Throwable throwable);
    }


    interface ProductsCallback {
        void onSuccess(List<Product> productList);

        void onFailure(Throwable throwable);
    }
}
