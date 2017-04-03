package com.appersiano.goustoexercise.repository.ormlite.repo;

import android.content.Context;

import com.appersiano.goustoexercise.repository.ormlite.db.DatabaseHelper;
import com.appersiano.goustoexercise.repository.ormlite.db.DatabaseManager;
import com.appersiano.goustoexercise.repository.ormlite.model.Attribute;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;
import com.appersiano.goustoexercise.repository.ormlite.model.Product;
import com.appersiano.goustoexercise.repository.ormlite.model.ProductAttribute;
import com.appersiano.goustoexercise.repository.ormlite.model.ProductCategory;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;


public class ProductRepo implements Crud{
	
	private DatabaseHelper helper;
	
	public ProductRepo(Context context)
	{
		DatabaseManager.init(context);
		helper = DatabaseManager.getInstance().getHelper();
	}

	@Override
	public int create(Object item) {
		
		int index = -1;
		
		Product object = (Product) item;
		try {
			index = helper.getProductsDao().create(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;

	}
	

	@Override
	public int update(Object item) {
		
		int index = -1;
		
		Product object = (Product) item;
		
		try {
			helper.getProductsDao().update(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}



	@Override
	public int delete(Object item) {
		
		int index = -1;
		
		Product object = (Product) item;
		
		try {
			helper.getProductsDao().delete(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}


	@Override
	public Object findById(int id) {
		
		Product wishList = null;
		try {
			wishList = helper.getProductsDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishList;
	}


	@Override
	public List<?> findAll() {
		
		List<Product> items = null;
		
		try {
			items =  helper.getProductsDao().queryForAll();

			//now add all categories related
			for (Product product : items) {

				List<Category> categories = lookupCategoryForProduct(product);
				product.setCategories(categories);

				List<Attribute> attributes = lookupAttributeForProduct(product);
				product.setAttributes(attributes);

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}

	private PreparedQuery<Category> categoriesForProductQuery = null;
	private PreparedQuery<Attribute> attributesForProductQuery = null;

	private List<Category> lookupCategoryForProduct(Product product) throws SQLException {
			if (categoriesForProductQuery == null) {
				categoriesForProductQuery = makeCategoriesForProductQuery();
			}
			categoriesForProductQuery.setArgumentHolderValue(0, product);

			return helper.getCategoryDao().query(categoriesForProductQuery);
	}

	private List<Attribute> lookupAttributeForProduct(Product product) throws SQLException {
		if (attributesForProductQuery == null) {
			attributesForProductQuery = makeAttributesForProductQuery();
		}
		attributesForProductQuery.setArgumentHolderValue(0, product);

		return helper.getAttributeDao().query(attributesForProductQuery);
	}


	/**
	 * Build our query for Categories objects that match a Product.
	 */
	private PreparedQuery<Category> makeCategoriesForProductQuery() throws SQLException {
		// build our inner query for ProductCategories objects
		QueryBuilder<ProductCategory, Integer> productCategory = helper.getProductCategoryDao().queryBuilder();

		// just select the category-id field
		productCategory.selectColumns(ProductCategory.CATEGORY_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();

		// you could also just pass in product1 here
		productCategory.where().eq(ProductCategory.PRODUCT_ID_FIELD_NAME, userSelectArg);

		// build our outer query for Post objects
		QueryBuilder<Category, Integer> postQb = helper.getCategoryDao().queryBuilder();
		// where the id matches in the post-id from the inner query
		postQb.where().in(Category.ID_FIELD_NAME, productCategory);

		return postQb.prepare();
	}

	/**
	 * Build our query for Attributes objects that match a Product.
	 */
	private PreparedQuery<Attribute> makeAttributesForProductQuery() throws SQLException {
		// build our inner query for ProductAttributes objects
		QueryBuilder<ProductAttribute, Integer> productAttribute = helper.getProductAttributeDao().queryBuilder();

		// just select the attribute-id field
		productAttribute.selectColumns(ProductAttribute.ATTRIBUTE_ID_FIELD_NAME);
		SelectArg userSelectArg = new SelectArg();

		// you could also just pass in product1 here
		productAttribute.where().eq(ProductAttribute.PRODUCT_ID_FIELD_NAME, userSelectArg);

		// build our outer query for Post objects
		QueryBuilder<Attribute, Integer> postQb = helper.getAttributeDao().queryBuilder();
		// where the id matches in the post-id from the inner query
		postQb.where().in(Attribute.ID_FIELD_NAME, productAttribute);

		return postQb.prepare();
	}

	/**
	 * Save the list of Products
	 * @param productList
	 */
	public void saveProductsDB(List<Product> productList){
		try {
			//Clean all stored data
			helper.clearAllTables();

			for (Product product : productList) {
				helper.getProductsDao().create(product);

				for (Category category : product.getCategories()) {
					//not needed for many to many
					//category.product = product;
					helper.getCategoryDao().createOrUpdate(category);

					// link the product and the category together in the join table
					ProductCategory product1category1 = new ProductCategory(product, category);
					helper.getProductCategoryDao().create(product1category1);
				}

				for (Attribute attribute : product.getAttributes()) {
					helper.getAttributeDao().createOrUpdate(attribute);

					// link the product and the attribute together in the join table
					ProductAttribute product1attribute1 = new ProductAttribute(product, attribute);
					helper.getProductAttributeDao().create(product1attribute1);
				}

//				for (Integer key : product.getImages().keySet()) {
//					Image currentImage = product.getImages().get(key);
//					currentImage.setProduct(product);
//					//helper.getImageDao().createOrUpdate(attribute);
//				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
