package pl.cubesoft.waldophotos.model.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pl.cubesoft.waldophotos.model.dto.Photo;
import pl.cubesoft.waldophotos.model.dto.Url;

/**
 * Created by CUBESOFT on 06.12.2016.
 */

public class ParcelablePhoto implements Parcelable {
    Photo photo;
    public ParcelablePhoto(Parcel in) {
        photo = new Photo();
        photo.setId(in.readString());
        List<Url> urls = new ArrayList<>();
        in.readList(urls, List.class.getClassLoader());
        photo.setUrls(urls);
    }

    public ParcelablePhoto(Photo photo) {
        this.photo  =photo;
    }

    public static final Creator<ParcelablePhoto> CREATOR = new Creator<ParcelablePhoto>() {
        @Override
        public ParcelablePhoto createFromParcel(Parcel in) {
            return new ParcelablePhoto(in);
        }

        @Override
        public ParcelablePhoto[] newArray(int size) {
            return new ParcelablePhoto[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(photo.getId());

        parcel.writeList(photo.getUrls());
    }

    public Photo getPhoto() {
        return photo;
    }
}
