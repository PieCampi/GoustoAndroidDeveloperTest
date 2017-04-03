package com.appersiano.goustoexercise.listadditionalproduct;

import android.support.annotation.NonNull;
import android.util.Log;

import com.appersiano.goustoexercise.repository.GoustoAPIRepo;
import com.appersiano.goustoexercise.repository.IGousto;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.util.Utils;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {
    public static final String TAG = "ProductDetailPresenter";
    private MainContract.View mView;
    private IGousto iGousto;

    public MainPresenter(@NonNull MainContract.View mainView, @NonNull IGousto iGousto) {
        this.mView = mainView;
        this.iGousto = iGousto;
    }

    @Override
    public void refreshCategories(){
        iGousto.getCategoryList(new GoustoAPIRepo.CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categoryList) {
                categoryList.add(0, new Category("0", "All Categories"));
                mView.setFilterCategory(Utils.getCategoriesNoHidden(categoryList));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, "onFailure: " + throwable.toString());
            }
        });

    }

    @Override
    public void refreshProducts() {
        iGousto.getProductWithInfo(mView.getScreenWidth(), new GoustoAPIRepo.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                mView.showProductsList(productList);
                mView.swipeSetRefreshing(false);
                mView.setMainProgressBarVisible(false);
                mView.showNoDataAvailableView(false);
            }

            @Override
            public void onFailure(Throwable throwable) {
                mView.swipeSetRefreshing(false);
                mView.setMainProgressBarVisible(false);
                mView.showNoDataAvailableView(true);
            }
        });
    }

    @Override
    public void start() {
        //init ll views
        mView.setMainProgressBarVisible(true);
        mView.showNoDataAvailableView(false);
        refreshCategories();
        refreshProducts();
    }
}