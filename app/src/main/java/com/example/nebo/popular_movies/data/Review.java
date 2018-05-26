package com.example.nebo.popular_movies.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {

    private static final int REVIEW_ID_CONTENT_VERSION = 1;
    private String mAuthor;
    private String mContent;
    private String mId;
    private String mUrl;

    public Review(String author, String content, String id, String url) {
        this.mAuthor = author;
        this.mContent = content;
        this.mId = id;
        this.mUrl = url;
    }

    private Review(Parcel src) {
        if (src != null) {
            this.mAuthor = src.readString();
            this.mContent = src.readString();
            this.mId = src.readString();
            this.mUrl = src.readString();
        }
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {

        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            if (size >= 0) {
                return new Review[size];
            }
            else {
                return new Review[0];
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
            dest.writeString(this.mAuthor);
            dest.writeString(this.mContent);
            dest.writeString(this.mId);
            dest.writeString(this.mUrl);
        }
    }
}
