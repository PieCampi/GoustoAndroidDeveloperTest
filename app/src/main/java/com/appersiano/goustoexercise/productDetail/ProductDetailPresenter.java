package com.appersiano.goustoexercise.productDetail;

import android.support.annotation.NonNull;

import com.appersiano.goustoexercise.repository.GoustoAPIRepo;
import com.appersiano.goustoexercise.repository.ormlite.model.Image;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    public static final String TAG = "ProductDetailPresenter";
    private ProductDetailContract.View mView;
    private Product product;

    public ProductDetailPresenter(@NonNull ProductDetailContract.View mainView, @NonNull Product product) {
        this.mView = mainView;
        this.product = product;

    }

    @Override
    public void start() {
        mView.setupToolbarTitle(product.getTitle());
        mView.setImageHeader(product);
        mView.displayCategories(product.getCategories());
        mView.setupProductDescription(product.getDescription());
        mView.hideAttributeView();
        mView.displayAttributes(product.getAttributes());
    }
}