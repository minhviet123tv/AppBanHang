package com.example.prm391x_searchfood_vietcvfx12045.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Blob;

public class Categories implements Serializable {
    private int id_categories;
    private String categories_name;
    private byte [] categories_image;
    private int views;

    public Categories() {

    }

    public Categories(int id_categories, String categories_name, byte[] categories_image, int views) {
        this.id_categories = id_categories;
        this.categories_name = categories_name;
        this.categories_image = categories_image;
        this.views = views;
    }

    public int getId_categories() {
        return id_categories;
    }

    public void setId_categories(int id_categories) {
        this.id_categories = id_categories;
    }

    public String getCategories_name() {
        return categories_name;
    }

    public void setCategories_name(String categories_name) {
        this.categories_name = categories_name;
    }

    public byte[] getCategories_image() {
        return categories_image;
    }

    public void setCategories_image(byte[] categories_image) {
        this.categories_image = categories_image;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
