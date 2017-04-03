package com.appersiano.goustoexercise.repository;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.appersiano.goustoexercise.R;
import com.appersiano.goustoexercise.repository.ormlite.db.DatabaseManager;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.repository.ormlite.repo.CategoryRepo;
import com.appersiano.goustoexercise.repository.ormlite.repo.ProductRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * GoustoAPIRepo is the Model(VP), this class make http requests to Gousto API and returns data.
 * This class include the caches functionalities (through the Category and Product repo),
 * when the user ask for data receive always (if are present) it, fresh is connection is available
 * cached if connections is not available
 */
public class GoustoAPIRepo implements IGousto {

    public static final String CATEGORIES = "categories";
    public static final String ATTRIBUTES = "attributes";
    private static final String TAG = "GoustoAPIRepo";
    private GoustoService service = null;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public GoustoAPIRepo(Context context) {

        Context mContext = context;

        //Init DB
        DatabaseManager.init(context);
        categoryRepo = new CategoryRepo(context);
        productRepo = new ProductRepo(context);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);

            }
        });


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mContext.getString(R.string.endpoint_api_gousto))
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        service = retrofit.create(GoustoService.class);
    }

    @Override
    public void getCategoryList(@NonNull final CategoriesCallback callback) {
        Call<GoustoResponse<List<Category>>> listCall = service.getCategories();
        listCall.enqueue(new Callback<GoustoResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<GoustoResponse<List<Category>>> call, Response<GoustoResponse<List<Category>>> response) {
                List<Category> categories = response.body().getData();

                callback.onSuccess(categories);
            }

            @Override
            public void onFailure(Call<GoustoResponse<List<Category>>> call, Throwable t) {
                List<Category> categories = null;

                //onFailure we show the cached data
                if (categoryRepo != null) {
                    categories = (List<Category>) categoryRepo.findAll();
                }

                if (categories != null && categories.size() > 0) {
                    callback.onSuccess(categories);
                }else{
                    callback.onFailure(t);
                    Log.e(TAG, "onFailure: " + t.toString());
                }

            }
        });
    }

    @Override
    public void getProductWithInfo(@NonNull Integer imageSize, @NonNull final ProductsCallback callback) {
        List<String> includes = new ArrayList<>(2);
        includes.add(CATEGORIES);
        includes.add(ATTRIBUTES);

        Call<GoustoResponse<List<Product>>> listCall = service.getProductsWithCAI(includes, imageSize);
        listCall.enqueue(new Callback<GoustoResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<GoustoResponse<List<Product>>> call, Response<GoustoResponse<List<Product>>> response) {
                final List<Product> products = response.body().getData();

                //Save the categories on the database in a worker thread
                //we have already data to display!
                new Thread(new Runnable() {
                    public void run() {
                        productRepo.saveProductsDB(products);
                    }
                }).start();

                callback.onSuccess(products);
            }

            @Override
            public void onFailure(Call<GoustoResponse<List<Product>>> call, Throwable t) {
                List<Product> productList = null;

                if (productRepo != null)
                    productList = (List<Product>) productRepo.findAll();

                if (productList != null && productList.size() > 0){
                    callback.onSuccess(productList);
                }else{
                    callback.onFailure(t);
                    Log.e(TAG, "onFailure: " + t.toString());
                }

            }
        });
    }

    public interface GoustoService {
        @GET("/products/v2.0/categories")
        Call<GoustoResponse<List<Category>>> getCategories();

        @GET("/products/v2.0/products")
        Call<GoustoResponse<List<Product>>> getProductsWithCAI(
                @Query("includes[]") List<String> include,
                @Query("image_sizes[]") Integer imageSize);
    }

}