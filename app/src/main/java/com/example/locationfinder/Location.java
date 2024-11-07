package com.example.locationfinder;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Location implements Parcelable {
    private int id;
    private String fullAddress;
    double longitude, latitude;
    public Location(int id, String fullAddress, double latitude, double longitude) {
        this.id = id;
        this.fullAddress = fullAddress;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Location(Parcel in){
        id = in.readInt();
        fullAddress = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fullAddress);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
