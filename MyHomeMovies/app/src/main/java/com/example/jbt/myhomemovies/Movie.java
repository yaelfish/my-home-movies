package com.example.jbt.myhomemovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jbt on 11/15/2016.
 */
public class Movie implements Parcelable {

    private long id;
    private String title;
    private String body;
    private String urlPoster;
    private int image;

    public Movie(long id, String title, String body, String urlPoster) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.urlPoster = urlPoster;
    }

    public Movie(String title, String body, String urlPoster) {
        this.title = title;
        this.body = body;
        this.urlPoster = urlPoster;
    }

    //  constructor that the CREATOR uses to create a new Movie from the Parcel
    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        body = in.readString();
        urlPoster = in.readString();
        image = in.readInt();
    }

    // a new anonymous class that implements Creator<>. This is used when creating a new Movie from the Parcel
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrlPoster() {
        return urlPoster;
    }

    public void setUrlPoster(String urlPoster) {
        this.urlPoster = urlPoster;
    }

    public long getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Movie title: " + title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // method to describe what to save to the Parcel. (which Movies we want to save)
    // the order of writing to the parcel is the same as reading in the constructor!
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeString(body);
        parcel.writeString(urlPoster);
        parcel.writeInt(image);
    }
}
