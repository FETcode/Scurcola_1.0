package com.example.leonardo.scurcola;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {
    private String mName; // Player's name
    private Card mCard; // Player's current card
    private boolean mLifeStatus = true; // Player's life status
    private boolean mProtected = false; // If the Player's been protected by the guard or not
    private int mId; // ID of the Player
    private int mCount = 0;

    /* everything below here is for implementing Parcelable */

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeParcelable(mCard, flags);
        out.writeInt(mLifeStatus ? 1 : 0);
        out.writeInt(mProtected ? 1 : 0);
        out.writeInt(mId);
        out.writeInt(mCount);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Player(Parcel in) {
        mName = in.readString();
        mCard = in.readParcelable(Card.class.getClassLoader());
        mLifeStatus = in.readInt() == 1;
        mProtected = in.readInt() == 1;
        mId = in.readInt();
        mCount = in.readInt();
    }

    public Player(){

    }

    public String getName(){ //Get player's name
        return mName;
    }
    public String getCardName(){ //Get Player's card's name
        return mCard.getName();
    }
    public boolean getLifeStatus(){ //See whether the Player is alive or not
        return mLifeStatus;
    }
    public boolean getProtected() {
        return mProtected;
    }
    public int getId(){
        return mId;
    }
    public int getCount() {
        return mCount;
    }


    public void setCard(Card card){ // Assign a card to the current Player
        mCard = card;
    }
    public void setName(String name){ // Assign a name to the current Player
        mName = name;
    }
    public void setLifeStatus(boolean status){
        mLifeStatus = status;
    }
    public void setProtected(boolean protect) {
        mProtected = protect;
    }
    public void setId(int id){
        mId = id;
    }
    public void incrementCount() {
        mCount++;
    }
    public void setCount(int count){
        mCount = count;
    }
}
