package com.appersiano.goustoexercise.listadditionalproduct;


import com.appersiano.goustoexercise.BasePresenter;
import com.appersiano.goustoexercise.BaseView;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;

import java.util.List;

public interface MainContract {

    interface View extends BaseView<Presenter> {

        void showProductsList(List<Product> productsList);

        Integer getScreenWidth();

        void swipeSetRefreshing(boolean setRefreshing);

        void setFilterCategory(List<Category> categoryList);

        void setMainProgressBarVisible(boolean isVisible);

        void showNoDataAvailableView(boolean isVisible);

    }

    interface Presenter extends BasePresenter {

        void refreshCategories();
        void refreshProducts();
    }
}
