package com.example.nebo.popular_movies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable{



    public Trailer() {

    }

    private Trailer(Parcel src) {
        if (src != null) {
            src.readString();
        }
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
