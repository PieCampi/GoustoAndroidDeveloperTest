package com.appersiano.goustoexercise;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.appersiano.goustoexercise.repository.GoustoAPIRepo;
import com.appersiano.goustoexercise.repository.IGousto;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.util.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * This test class make Gousto API request and check the data size return
 */
@RunWith(AndroidJUnit4.class)
public class GoustoApiITest {
    private static final String TAG = "GustoApiTest";

    @Test
    public void testGetCategories() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assumeTrue("Device must be connected to internet", Utils.isConnected(appContext));

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GoustoAPIRepo goustoAPIRepo = new GoustoAPIRepo(appContext);

        goustoAPIRepo.getCategoryList(new IGousto.CategoriesCallback() {
            @Override
            public void onSuccess(List<Category> categoryList) {
                countDownLatch.countDown();
                assertTrue("Gousto has " + categoryList.size() + " categories",
                        categoryList.size() > 0);
            }

            @Override
            public void onFailure(Throwable throwable) {
                countDownLatch.countDown();
                fail("Call failed " + throwable.toString());
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetProducts() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        assumeTrue("Device must be connected to internet", Utils.isConnected(appContext));

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        GoustoAPIRepo goustoAPIRepo = new GoustoAPIRepo(appContext);

        goustoAPIRepo.getProductWithInfo(300, new IGousto.ProductsCallback() {
            @Override
            public void onSuccess(List<Product> productList) {
                countDownLatch.countDown();
                assertTrue("Gousto has " + productList.size() + " products available",
                        productList.size() > 0);
            }

            @Override
            public void onFailure(Throwable throwable) {
                countDownLatch.countDown();
                fail("Call failed " + throwable.toString());
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

