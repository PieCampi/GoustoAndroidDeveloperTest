package com.appersiano.goustoexercise;

import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.util.Utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilUnitTest {

    @Test
    public void getCategoriesNoHidden() throws Exception {
        List<Category> categories = new ArrayList<>();
        Category category1 = new Category("id1", "value1", false);
        Category category2 = new Category("id2", "value2", true);

        categories.add(category1);
        categories.add(category2);

        assertTrue("Size of filtered category list is one", Utils.getCategoriesNoHidden(categories).size() == 1);
    }
}