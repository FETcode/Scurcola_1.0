package com.example.leonardo.scurcola;

import android.os.Parcel;
import android.os.Parcelable;



public class Card implements Parcelable{
    private String mName;


    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    private Card(Parcel in) {
        mName = in.readString();
    }

    public Card(){

    }



    public String getName(){
        return mName;
    }
    public void setName(String name){
        mName = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
    }
}
