package com.example.nebo.popular_movies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Trailer implements Parcelable{
    private String mId;
    private String mKey;
    private String mSite;
    private int mSize;
    private String mType;

    public Trailer(String id, String key, String site, int size, String type) {
        this.mId = id;
        this.mKey = key;
        this.mSite = site;
        this.mSize = size;
        this.mType = type;
    }

    private Trailer(Parcel src) {
        if (src != null) {
            this.mId = src.readString();
            this.mKey = src.readString();
            this.mSite = src.readString();
            this.mSize = src.readInt();
            this.mType = src.readString();
        }
    }

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            if (source == null) {
                return null;
            }
            else {
                return new Trailer(source);
            }
        }

        @Override
        public Trailer[] newArray(int size) {
            if (size > 0) {
                return new Trailer[size];
            }
            else {
                return new Trailer[0];
            }
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dest != null) {
            dest.writeString(this.mId);
            dest.writeString(this.mKey);
            dest.writeString(this.mSite);
            dest.writeInt(this.mSize);
            dest.writeString(this.mType);
        }
    }
}
