package com.appersiano.goustoexercise.repository.ormlite.repo;

import android.content.Context;

import com.appersiano.goustoexercise.repository.ormlite.db.DatabaseHelper;
import com.appersiano.goustoexercise.repository.ormlite.db.DatabaseManager;
import com.appersiano.goustoexercise.repository.ormlite.model.Category;

import java.sql.SQLException;
import java.util.List;


public class CategoryRepo implements Crud{
	
	private DatabaseHelper helper;
	
	public CategoryRepo(Context context)
	{
		DatabaseManager.init(context);
		helper = DatabaseManager.getInstance().getHelper();
	}

	@Override
	public int create(Object item) {
		
		int index = -1;
		
		Category object = (Category) item;
		try {
			index = helper.getCategoryDao().create(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;

	}
	

	@Override
	public int update(Object item) {
		
		int index = -1;
		
		Category object = (Category) item;
		
		try {
			helper.getCategoryDao().update(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}



	@Override
	public int delete(Object item) {
		
		int index = -1;
		
		Category object = (Category) item;
		
		try {
			helper.getCategoryDao().delete(object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return index;
	}


	@Override
	public Object findById(int id) {
		
		Category wishList = null;
		try {
			wishList = helper.getCategoryDao().queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wishList;
	}


	@Override
	public List<?> findAll() {
		
		List<Category> items = null;
		
		try {
			items =  helper.getCategoryDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return items;
	}

	public void saveCategoriesDB(List<Category> categoryList){
		try {
			helper.clearCategoryTable();
			helper.getCategoryDao().create(categoryList);
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
}
