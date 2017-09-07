package com.ywp.yi.pets;

/**
 * Created by YI on 2017/8/24.
 */

public class petsList {

    private String mPetName;
    private String mPetBreed;
    public petsList(String petName,String petBreed){
        mPetName = petName;
        mPetBreed = petBreed;
    }
    public String getPetName() {
        return mPetName;
    }

    public String getPetBreed() {
        return mPetBreed;
    }
}
