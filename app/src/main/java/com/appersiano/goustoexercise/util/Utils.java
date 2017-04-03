package com.appersiano.goustoexercise.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.appersiano.goustoexercise.repository.ormlite.model.Category;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Return the width of number of pixel inside a dip
     * @param context
     * @return
     */
    public static int getWidthOfScreen(Context context){

        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return Math.round(displayMetrics.widthPixels * context.getResources().getDisplayMetrics().density);
    }

    /**
     * Check if the device is connected to Internet
     *
     * @return true -> Connected , false -> NON Connected
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static List<Category> getCategoriesNoHidden(List<Category> categoryList) {
        List<Category> filteredCategory = new ArrayList<>(categoryList.size());

        for (Category category : categoryList) {
            if (!category.getHidden()) {
                filteredCategory.add(category);
            }
        }

        return filteredCategory;
    }
}
