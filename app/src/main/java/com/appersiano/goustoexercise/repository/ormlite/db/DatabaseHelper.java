package com.appersiano.goustoexercise.repository.ormlite.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.appersiano.goustoexercise.repository.ormlite.model.Attribute;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.repository.ormlite.model.ProductAttribute;
import com.appersiano.goustoexercise.repository.ormlite.model.ProductCategory;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "GoustoDB.sqlite";

    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    //pressure
    private Dao<Product, Integer> productsDao = null;
    private Dao<Category, Integer> categoryDao = null;
    private Dao<Attribute, Integer> attributeDao = null;
    private Dao<ProductCategory, Integer> productCategoryDao = null;
    private Dao<ProductAttribute, Integer> productAttributeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, Product.class);
            TableUtils.createTable(connectionSource, Attribute.class);
            TableUtils.createTable(connectionSource, ProductCategory.class);
            TableUtils.createTable(connectionSource, ProductAttribute.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            List<String> allSql = new ArrayList<String>();
            switch (oldVersion) {
                case 1:
                    //allSql.add("alter table AdData add column `new_col` VARCHAR");
                    //allSql.add("alter table AdData add column `new_col2` VARCHAR");
            }
            for (String sql : allSql) {
                db.execSQL(sql);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "exception during onUpgrade", e);
            throw new RuntimeException(e);
        }

    }



    public Dao<Product, Integer> getProductsDao() {
        if (productsDao == null) {
            try {
                productsDao = getDao(Product.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return productsDao;
    }

    public Dao<Category, Integer> getCategoryDao() {
        if (categoryDao == null) {
            try {
                categoryDao = getDao(Category.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return categoryDao;
    }

    public Dao<Attribute, Integer> getAttributeDao() {
        if (attributeDao == null) {
            try {
                attributeDao = getDao(Attribute.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return attributeDao;
    }

    public Dao<ProductCategory, Integer> getProductCategoryDao() {
        if (productCategoryDao == null) {
            try {
                productCategoryDao = getDao(ProductCategory.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return productCategoryDao;
    }

    public Dao<ProductAttribute, Integer> getProductAttributeDao() {
        if (productAttributeDao == null) {
            try {
                productAttributeDao = getDao(ProductAttribute.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return productAttributeDao;
    }

    public void clearAllTables(){
        clearProductTable();
        clearCategoryTable();
        clearProductCategoryTable();
        clearAttributeTable();
        clearProductAttributeTable();
    }

    public void clearProductTable(){
        try {
            TableUtils.clearTable(getConnectionSource(), Product.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCategoryTable(){
        try {
            TableUtils.clearTable(getConnectionSource(), Category.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearAttributeTable(){
        try {
            TableUtils.clearTable(getConnectionSource(), Attribute.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearProductCategoryTable(){
        try {
            TableUtils.clearTable(getConnectionSource(), ProductCategory.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearProductAttributeTable(){
        try {
            TableUtils.clearTable(getConnectionSource(), ProductAttribute.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

}