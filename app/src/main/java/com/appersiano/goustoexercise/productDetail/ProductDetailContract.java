package com.appersiano.goustoexercise.productDetail;


import android.content.Context;

import com.appersiano.goustoexercise.BasePresenter;
import com.appersiano.goustoexercise.BaseView;
import com.appersiano.goustoexercise.repository.ormlite.model.Attribute;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;

import java.util.Collection;

public interface ProductDetailContract {

    interface View extends BaseView<Presenter> {

        void displayAttributes(Collection<Attribute> attributeCollection);

        void displayCategories(Collection<Category> categoryCollection);

        void setImageHeader(Product currentProduct);

        void hideAttributeView();

        void setupToolbarTitle(String title);

        void setupProductDescription(String description);

    }

    interface Presenter extends BasePresenter {
    }
}
