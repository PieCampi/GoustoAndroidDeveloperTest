package com.appersiano.goustoexercise.repository.ormlite.model;

import android.content.Context;

import com.appersiano.goustoexercise.util.AttributeCollectionConverter;
import com.appersiano.goustoexercise.util.CategoryCollectionConverter;
import com.appersiano.goustoexercise.util.Utils;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.parceler.Parcel;
import org.parceler.ParcelPropertyConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parcel
@DatabaseTable(tableName = "product")
public class Product{

    private static final String MOST_POPULAR = "Most Popular";

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String sku;
    @DatabaseField
    private String title;
    @DatabaseField
    private String description;
    @DatabaseField
    private String list_price;
    @DatabaseField
    private Boolean is_vatable;
    @DatabaseField
    private Boolean is_for_sale;
    @DatabaseField
    private Boolean age_restricted;
    @DatabaseField
    private Short box_limit;
    @DatabaseField
    private Boolean always_on_menu;
    @DatabaseField
    private Date created_at;

    @ParcelPropertyConverter(CategoryCollectionConverter.class)
    private Collection<Category> categories = new ArrayList<>();

    @ParcelPropertyConverter(AttributeCollectionConverter.class)
    private Collection<Attribute> attributes = new ArrayList<>();

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private HashMap<String, Image> images = new HashMap<>();

    private transient Image noImageAvailable = new Image(null);

    public Image getImageLink(Context context) {
        Image imageRet = images.get(String.valueOf(Utils.getWidthOfScreen(context)));
        if (imageRet == null) {
            return noImageAvailable;
        } else {
            return imageRet;
        }
    }

    public String getList_price() {
        return "£ " + list_price;
    }

    public String getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIs_vatable() {
        return is_vatable;
    }

    public Boolean getIs_for_sale() {
        return is_for_sale;
    }

    public Boolean getAge_restricted() {
        return age_restricted;
    }

    public Short getBox_limit() {
        return box_limit;
    }

    public Boolean getAlways_on_menu() {
        return always_on_menu;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    public Map<String, Image> getImages() {
        return images;
    }

    public Image getNoImageAvailable() {
        return noImageAvailable;
    }

    /**
     * Check if a product is a "Most Popular"
     * @return true if is popular, false otherwise
     */
    public boolean isPopular() {
        for (Category category : categories) {
            if (category.getTitle().equals(MOST_POPULAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the categories related to the product
     * @param includeHidden include attribute that has hidden property
     * @return All the categories filtered on includeHidden param
     */
    public Collection<Category> getCategories(boolean includeHidden) {

        if (includeHidden) {

            return getCategories();

        } else {

            List<Category> filteredCategories = Utils.getCategoriesNoHidden(new ArrayList<>(categories));

            Collections.sort(filteredCategories);

            return filteredCategories;
        }
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public void setAttributes(Collection<Attribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}