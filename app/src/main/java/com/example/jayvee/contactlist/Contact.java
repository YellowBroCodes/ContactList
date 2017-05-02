package com.example.jayvee.contactlist;

import android.net.Uri;

public class Contact {

    private Uri imageUri;
    private String name, tel;

    public Contact(Uri imageUri, String name, String tel) {
        super();
        this.imageUri = imageUri;
        this.name = name;
        this.tel = tel;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
