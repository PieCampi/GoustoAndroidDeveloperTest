package com.appersiano.goustoexercise.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appersiano.goustoexercise.R;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Image;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.productDetail.ProductDetailActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable{

    private List<Product> mProducts;
    private List<Product> filteredProducts = new ArrayList<>();
    private Context mContext;
    private Activity mActivity;
    private int lastPosition = -1;

    public void resetLastPosition() {
        this.lastPosition = -1;
    }

    public ProductAdapter(Context context, Activity activity) {
        mProducts = null;
        mContext = context;
        mActivity = activity;
    }

    public List<Product> getmProducts() {
        return mProducts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View stargazerView = inflater.inflate(R.layout.product_card, parent, false);

        return new ViewHolder(stargazerView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Product product = filteredProducts.get(position);

        //Init Views
        holder.progressDialog.setVisibility(View.INVISIBLE);
        holder.errorImageLL.setVisibility(View.INVISIBLE);
        holder.noImageLL.setVisibility(View.INVISIBLE);

        final Image image = product.getImageLink(mContext);

        String urlImage = (image != null)? image.url : null;

        //If there are no image, put a custom no_image res
        if (urlImage == null){
            holder.background.setImageDrawable(null);
            holder.background.setVisibility(View.INVISIBLE);
            holder.progressDialog.setVisibility(View.INVISIBLE);
            holder.errorImageLL.setVisibility(View.INVISIBLE);

            //Show view no image
            holder.noImageLL.setVisibility(View.VISIBLE);

        }else{
            //Show only progress dialog
            holder.progressDialog.setVisibility(View.VISIBLE);
            holder.noImageLL.setVisibility(View.INVISIBLE);
            holder.errorImageLL.setVisibility(View.INVISIBLE);

            Picasso.with(mContext)
                    .load(urlImage)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.background, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.background.setVisibility(View.VISIBLE);
                            holder.progressDialog.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.progressDialog.setVisibility(View.GONE);
                            holder.errorImageLL.setVisibility(View.VISIBLE);
                        }
                    });
        }

        if (product.isPopular()){
            holder.mostPopularBadge.setVisibility(View.VISIBLE);
        }else {
            holder.mostPopularBadge.setVisibility(View.INVISIBLE);
        }



        holder.title.setText(product.getTitle());
        holder.price.setText(product.getList_price());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ProductDetailActivity.class);

                // Pass data object in the bundle and populate details activity.
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT, Parcels.wrap(product));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mActivity, holder.background, "productImg");

                mActivity.startActivity(intent, options.toBundle());
            }
        });

        // Set the view to fade in
        setScaleAnimation(holder.itemView, position);
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        holder.itemView.clearAnimation();
    }


    private void setScaleAnimation(View view, int position) {
        if(position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(700);
            view.startAnimation(anim);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        if (filteredProducts != null)
            return filteredProducts.size();
        else
            return 0;
    }

    public void clearData() {
        if (filteredProducts != null) {
            filteredProducts.clear();
            notifyDataSetChanged();
        }else{
            filteredProducts = new ArrayList<>();
        }
    }

    public void initializeData(List<Product> productList) {
        if (mProducts == null) {
            mProducts = new ArrayList<>();
        }
        mProducts.clear();
        mProducts.addAll(productList);

        //We check if filtered is empty for swiperefresh functionality, is important!
        if (filteredProducts.isEmpty())
            filteredProducts.addAll(productList);

        notifyDataSetChanged();
    }

    public void forceRefresh(List<Product> productList) {
        List<Product> temp = new ArrayList<>(productList);
        filteredProducts.clear();
        filteredProducts.addAll(temp);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                resetLastPosition();
                filteredProducts.clear();
                final FilterResults results = new FilterResults();

                if (constraint.equals("0")) {
                    if (mProducts != null)
                        filteredProducts.addAll(mProducts);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Product product : mProducts) {
                        for (Category category : product.getCategories(false)){
                            if (category.getId().equals(filterPattern) || filterPattern.equals("0")){
                                filteredProducts.add(product);
                            }
                        }
                    }
                }
                results.values = filteredProducts;
                results.count = filteredProducts.size();
                return results;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Product> displayList = (ArrayList<Product>) results.values;
                if (displayList != null && displayList.size() > 0) {
                    forceRefresh((ArrayList<Product>) results.values);
                }
            }
        };

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView background;
        TextView title;
        TextView price;
        ProgressBar progressDialog;
        ImageView mostPopularBadge;
        LinearLayout noImageLL, errorImageLL;

        ViewHolder(View itemView) {
            super(itemView);

            background = (ImageView) itemView.findViewById(R.id.backgroundProduct);
            title = (TextView) itemView.findViewById(R.id.titleTv);
            price = (TextView) itemView.findViewById(R.id.priceTv);
            progressDialog = (ProgressBar) itemView.findViewById(R.id.progress);
            cardView = (CardView) itemView.findViewById(R.id.cardProduct);
            mostPopularBadge = (ImageView) itemView.findViewById(R.id.mostPopulareImg);
            noImageLL = (LinearLayout) itemView.findViewById(R.id.noImagesLL);
            errorImageLL = (LinearLayout) itemView.findViewById(R.id.errorLoadImgLL);
        }
    }
}

