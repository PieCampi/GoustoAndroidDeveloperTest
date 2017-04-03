package com.appersiano.goustoexercise.repository.ormlite.model;

import com.j256.ormlite.field.DatabaseField;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class Image implements Serializable {

    public final String ACCOUNT_ID_FIELD_NAME = "product_ref";

    @DatabaseField
    public String src;
    @DatabaseField(id = true)
    public String url;

    @DatabaseField
    public Integer width;

    public Image(String src) {
        this.src = src;
        this.url = src;
    }

    public Image() {
        //needed for ormlite
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
