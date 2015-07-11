package com.skala.runloop_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Skala
 */
public class MemberModel implements Parcelable {
    private String mImageURL;
    private String mFullName;
    private String mPosition;
    private String mDescription;

    public MemberModel(String imageURL, String fullName, String position, String description) {
        mImageURL = imageURL;
        mFullName = fullName;
        mPosition = position;
        mDescription = description;
    }

    private MemberModel(Parcel in) {
        mImageURL = in.readString();
        mFullName = in.readString();
        mPosition = in.readString();
        mDescription = in.readString();
    }

    public static final Creator<MemberModel> CREATOR = new Creator<MemberModel>() {
        @Override
        public MemberModel createFromParcel(Parcel in) {
            return new MemberModel(in);
        }

        @Override
        public MemberModel[] newArray(int size) {
            return new MemberModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageURL);
        dest.writeString(mFullName);
        dest.writeString(mPosition);
        dest.writeString(mDescription);
    }

    public String getImageURL() {
        return mImageURL;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getPosition() {
        return mPosition;
    }

    public String getDescription() {
        return mDescription;
    }
}
