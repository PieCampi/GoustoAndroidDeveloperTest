package com.appersiano.goustoexercise.util;

import android.os.Parcel;

import com.appersiano.goustoexercise.repository.ormlite.model.Category;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryCollectionConverter implements ParcelConverter<Collection<Category>> {
    @Override
    public void toParcel(Collection<Category> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (Category item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public Collection<Category> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<Category> items = new ArrayList<Category>();
        for (int i = 0; i < size; ++i) {
            items.add((Category) Parcels.unwrap(parcel.readParcelable(Category.class.getClassLoader())));
        }
        return items;
    }
}
