package com.appersiano.goustoexercise;


import android.support.annotation.NonNull;

import com.appersiano.goustoexercise.listadditionalproduct.MainContract;
import com.appersiano.goustoexercise.listadditionalproduct.MainPresenter;
import com.appersiano.goustoexercise.repository.IGousto;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

public class MainPresenterTest {

    @Mock
    private MainContract.View view;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fetchValidDataShouldLoadIntoView() {

        //Mock the response
        Product product = new Product();
        final List<Product> specialProductList = new ArrayList<>();
        specialProductList.add(product);

        final List<Category> specialCategoryList = new ArrayList<>();
        Category category1 = new Category("firstId","Example");
        Category category2 = new Category("secondId","Example2");
        specialCategoryList.add(category1);
        specialCategoryList.add(category2);

        IGousto gg = new IGousto() {
            @Override
            public void getCategoryList(@NonNull CategoriesCallback callback) {
                callback.onSuccess(specialCategoryList);
            }

            @Override
            public void getProductWithInfo(@NonNull Integer imageSize, @NonNull ProductsCallback callback) {
                callback.onSuccess(specialProductList);
            }
        };

        MainPresenter mainPresenter = new MainPresenter(this.view, gg);

        mainPresenter.start();

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view, times(1)).setMainProgressBarVisible(true);
        inOrder.verify(view, times(1)).showNoDataAvailableView(false);
        inOrder.verify(view, times(1)).setFilterCategory(specialCategoryList);
        inOrder.verify(view, times(1)).showProductsList(specialProductList);
    }

}