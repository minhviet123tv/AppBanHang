package com.example.prm391x_searchfood_vietcvfx12045.model;

import java.util.Arrays;

public class Product {
    private int id_product, id_categories;
    private String product_name;
    private byte [] product_image;
    private String description;
    private double price;
    private String unit;
    private Long views;

    public Product (){

    }

    public Product(int id_product, int id_categories, String product_name, byte [] product_image, String description, double price, String unit, Long views) {
        this.id_product = id_product;
        this.id_categories = id_categories;
        this.product_name = product_name;
        this.product_image = product_image;
        this.description = description;
        this.price = price;
        this.unit = unit;
        this.views = views;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getId_categories() {
        return id_categories;
    }

    public void setId_categories(int id_categories) {
        this.id_categories = id_categories;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public byte[] getProduct_image() {
        return product_image;
    }

    public void setProduct_image(byte[] product_image) {
        this.product_image = product_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id_product=" + id_product +
                ", id_categories=" + id_categories +
                ", product_name='" + product_name + '\'' +

                ", description='" + description + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", views=" + views +
                '}';
    }
}
