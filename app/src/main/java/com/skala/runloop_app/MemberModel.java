package com.skala.runloop_app;

/**
 * @author Skala
 */
public class MemberModel {
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
