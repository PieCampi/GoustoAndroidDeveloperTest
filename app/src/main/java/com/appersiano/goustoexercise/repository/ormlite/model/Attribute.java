package com.appersiano.goustoexercise.repository.ormlite.model;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.parceler.Parcel;

@Parcel
@DatabaseTable(tableName = "attribute")
public class Attribute {

    public static final String T_UNIT = "t_unit";
    public static final String ID_FIELD_NAME = "attribute_id";

    public static final String ALLERGEN = "Allergen";
    public static final String VOLUME = "Volume";
    public static final String WEIGHT = "Weight";

    @DatabaseField(columnName = ID_FIELD_NAME, generatedId = true)
    private int generatedID;

    @DatabaseField
    private String id;
    @DatabaseField
    private String title;
    @DatabaseField(columnName = T_UNIT)
    private String unit;
    @DatabaseField
    private String value;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUnit() {
        return (unit == null)? "" : unit;
    }

    public String getValue() {
        return value;
    }
}
