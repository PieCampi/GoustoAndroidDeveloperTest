package com.appersiano.goustoexercise.listadditionalproduct;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appersiano.goustoexercise.R;
import com.appersiano.goustoexercise.repository.GoustoAPIRepo;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.util.ProductAdapter;
import com.appersiano.goustoexercise.util.Utils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String MAIN = "MAIN";
    public static final String TAG = "MainActivity";
    private MainContract.Presenter mPresenter;
    private ProductAdapter productAdapter;
    private RecyclerView recyclerProducts;
    private SwipeRefreshLayout swipeContainer;
    private Spinner spinnerCategories;
    private Toolbar toolbar;
    private ProgressBar progressBarMain;
    private TextView noDataAvailable;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;
    private BroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    @Override
    public void showNoDataAvailableView(boolean isVisible) {
        if (isVisible) {
            noDataAvailable.setVisibility(View.VISIBLE);
        } else {
            noDataAvailable.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setMainProgressBarVisible(boolean isVisible) {

        if (isVisible) {
            progressBarMain.setVisibility(View.VISIBLE);
        } else {
            progressBarMain.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public Integer getScreenWidth() {
        return Utils.getWidthOfScreen(this);
    }

    @Override
    public void showProductsList(List<Product> productsList) {
        spinnerCategories.setEnabled(true);
        productAdapter.initializeData(productsList);
    }

    @Override
    public void swipeSetRefreshing(boolean setRefreshing) {
        if (swipeContainer != null) {
            swipeContainer.setRefreshing(setRefreshing);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        GoustoAPIRepo goustoAPIRepo = new GoustoAPIRepo(getBaseContext());

        MainPresenter mainPresenter = new MainPresenter(this, goustoAPIRepo);
        setPresenter(mainPresenter);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recyclerProducts = (RecyclerView) findViewById(R.id.recyclerProducts);
        spinnerCategories = (Spinner) findViewById(R.id.spinnerCategory);
        ArrayAdapter defaultArrayAdapter = ArrayAdapter.createFromResource(this, R.array.no_selection, R.layout.support_simple_spinner_dropdown_item);
        spinnerCategories.setAdapter(defaultArrayAdapter);
        spinnerCategories.setEnabled(false);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBarMain = (ProgressBar) findViewById(R.id.progressBarMain);
        noDataAvailable = (TextView) findViewById(R.id.noDataAvailable);

        //Conf toolbar
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshProducts();
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mPresenter.start();

        setupProductRecycler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        //filter.addAction(Intent.);
        this.registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    public void setFilterCategory(final List<Category> categoryList) {

        final ArrayAdapter<Category> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item_sel, android.R.id.text1, categoryList);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinnerCategories
        spinnerCategories.setAdapter(adapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + position);
                productAdapter.getFilter().filter(categoryList.get(position).getId());
                recyclerProducts.scrollToPosition(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setupProductRecycler() {
        productAdapter = new ProductAdapter(getBaseContext(), this);
        recyclerProducts.setAdapter(productAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerProducts.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {

            if (!Utils.isConnected(context)){
                snackbar = Snackbar
                        .make(coordinatorLayout, "Check Internet connection...", Snackbar.LENGTH_INDEFINITE);

                snackbar.show();
            }else{
                if (snackbar != null)
                    snackbar.dismiss();

                if (mPresenter != null && productAdapter.getItemCount() == 0)
                    mPresenter.start();

            }
        }
    }

}
