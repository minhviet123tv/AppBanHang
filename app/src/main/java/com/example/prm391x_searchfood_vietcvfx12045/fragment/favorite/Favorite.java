package com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite;

public class Favorite {
    private byte [] image_product;
    private String product_name, product_unit;
    private double product_price;
    private int id_favorite, id_account, id_product, status;
    private String date_register;

    public Favorite() {

    }

    public Favorite(byte[] image_product, String product_name, String product_unit, double product_price, int id_favorite, int id_account, int id_product, int status, String date_register) {
        this.image_product = image_product;
        this.product_name = product_name;
        this.product_unit = product_unit;
        this.product_price = product_price;
        this.id_favorite = id_favorite;
        this.id_account = id_account;
        this.id_product = id_product;
        this.status = status;
        this.date_register = date_register;
    }

    public byte[] getImage_product() {
        return image_product;
    }

    public void setImage_product(byte[] image_product) {
        this.image_product = image_product;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_unit() {
        return product_unit;
    }

    public void setProduct_unit(String product_unit) {
        this.product_unit = product_unit;
    }

    public double getProduct_price() {
        return product_price;
    }

    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }

    public int getId_favorite() {
        return id_favorite;
    }

    public void setId_favorite(int id_favorite) {
        this.id_favorite = id_favorite;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate_register() {
        return date_register;
    }

    public void setDate_register(String date_register) {
        this.date_register = date_register;
    }
}
