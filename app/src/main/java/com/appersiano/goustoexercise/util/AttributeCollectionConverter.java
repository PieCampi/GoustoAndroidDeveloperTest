package com.appersiano.goustoexercise.util;

import android.os.Parcel;

import com.appersiano.goustoexercise.repository.ormlite.model.Attribute;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AttributeCollectionConverter implements ParcelConverter<Collection<Attribute>> {
    @Override
    public void toParcel(Collection<Attribute> input, Parcel parcel) {
        if (input == null) {
            parcel.writeInt(-1);
        }
        else {
            parcel.writeInt(input.size());
            for (Attribute item : input) {
                parcel.writeParcelable(Parcels.wrap(item), 0);
            }
        }
    }

    @Override
    public Collection<Attribute> fromParcel(Parcel parcel) {
        int size = parcel.readInt();
        if (size < 0) return null;
        List<Attribute> items = new ArrayList<Attribute>();
        for (int i = 0; i < size; ++i) {
            items.add((Attribute) Parcels.unwrap(parcel.readParcelable(Attribute.class.getClassLoader())));
        }
        return items;
    }
}
