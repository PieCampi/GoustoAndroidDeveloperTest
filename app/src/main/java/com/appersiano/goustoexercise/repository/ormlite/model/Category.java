package com.appersiano.goustoexercise.repository.ormlite.model;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.parceler.Parcel;

@Parcel
@DatabaseTable(tableName = "category")
public class Category implements Comparable<Category>{

    public static final String ID_FIELD_NAME = "category_id";

    @DatabaseField(id = true, columnName = ID_FIELD_NAME)
    private String id;
    @DatabaseField
    private String title;
    @DatabaseField
    private Short box_limit;
    @DatabaseField
    private Boolean is_default;
    @DatabaseField
    private Boolean recently_added;
    @DatabaseField
    private Boolean hidden;
    //TODO Add pivot element, actually I do not need this

    public Category() {
    }

    public Category(String id, String title) {
        this.id = id;
        this.title = title;
        this.hidden = false;
    }

    public Category(String id, String title, boolean hidden) {
        this.id = id;
        this.title = title;
        this.hidden = hidden;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Short getBox_limit() {
        return box_limit;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public Boolean getRecently_added() {
        return recently_added;
    }

    public Boolean getHidden() {
        return hidden;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public int compareTo(@NonNull Category o) {
        return this.getId().compareTo(o.getId());
    }
}
