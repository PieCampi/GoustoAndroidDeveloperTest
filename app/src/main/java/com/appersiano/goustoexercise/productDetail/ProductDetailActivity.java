package com.appersiano.goustoexercise.productDetail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appersiano.goustoexercise.R;
import com.appersiano.goustoexercise.repository.ormlite.model.Attribute;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Image;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.util.LabelTextView;
import com.paulyung.laybellayout.LaybelLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.Collection;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    public static final String TAG = "ProductDetail";
    public static final String EXTRA_PRODUCT = "extra_product";
    private ProductDetailContract.Presenter mPresenter;
    private Toolbar toolbar;
    private LaybelLayout categoryContainer;
    private View allergensConstraint, volumeConstraint, weightConstraint;
    private TextView allergenDesc, volumeDesc, weightDesc;
    private TextView description;
    private ImageView imageHeader;


    @Override
    public void displayAttributes(Collection<Attribute> attributeCollection) {

        for (Attribute attribute : attributeCollection) {
            if (attribute.getTitle().equals(Attribute.ALLERGEN)) {

                allergensConstraint.setVisibility(View.VISIBLE);
                allergenDesc.setText(attribute.getValue());

            } else if (attribute.getTitle().equals(Attribute.VOLUME)) {

                volumeConstraint.setVisibility(View.VISIBLE);
                volumeDesc.setText(attribute.getValue() + " " + attribute.getUnit());

            } else if (attribute.getTitle().equals(Attribute.WEIGHT)) {

                weightConstraint.setVisibility(View.VISIBLE);
                weightDesc.setText(attribute.getValue() + " " + attribute.getUnit());

            }
        }
    }

    @Override
    public void displayCategories(Collection<Category> categoryCollection) {

        for (Category category : categoryCollection) {
            LabelTextView.LabelTextViewBuilder builder = new LabelTextView.LabelTextViewBuilder(this);

            builder.setText(category.getTitle());
            builder.setDefaultBackgroundColor(getResources().getColor(R.color.colorAccent));
            builder.setTextPressedColor(android.R.color.white);
            builder.setPressedBackgroundColor(getResources().getColor(R.color.colorAccent));

            categoryContainer.addView(builder.build());
        }
    }

    @Override
    public void setImageHeader(Product product) {

        final Image image = product.getImageLink(this);
        String urlImage = (image != null) ? image.url : null;

        // /If there are no image, put a custom no_image res
        if (urlImage == null) {
            supportStartPostponedEnterTransition();
            //imageHeader.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
        } else {

            Picasso.with(this)
                    .load(urlImage)
                    .into(imageHeader, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                            //imageHeader.setImageDrawable(getResources().getDrawable(R.drawable.error_image));
                        }
                    });
        }
    }

    @Override
    public void hideAttributeView() {
        //Attributes
        allergensConstraint.setVisibility(View.GONE);
        volumeConstraint.setVisibility(View.GONE);
        weightConstraint.setVisibility(View.GONE);

    }

    @Override
    public void setupToolbarTitle(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void setupProductDescription(String desc) {
        description.setText(desc);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_detail);

        supportPostponeEnterTransition();

        //Get view references
        imageHeader = (ImageView) findViewById(R.id.backgroundProduct);
        description = (TextView) findViewById(R.id.description);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        categoryContainer = (LaybelLayout) findViewById(R.id.categoryContainer);
        allergensConstraint = (View) findViewById(R.id.allergesConstraint);
        allergenDesc = (TextView) findViewById(R.id.allergensDesc);
        volumeConstraint = (View) findViewById(R.id.volumeConstraint);
        volumeDesc = (TextView) findViewById(R.id.volumeDesc);
        weightConstraint = (View) findViewById(R.id.weightConstraint);
        weightDesc = (TextView) findViewById(R.id.weightDesc);


        Product product = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_PRODUCT));

        ProductDetailPresenter mainPresenter = new ProductDetailPresenter(this, product);
        setPresenter(mainPresenter);


        mPresenter.start();
    }


    @Override
    public void setPresenter(@NonNull ProductDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
