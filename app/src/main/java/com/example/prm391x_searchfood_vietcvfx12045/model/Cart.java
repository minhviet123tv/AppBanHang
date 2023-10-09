package com.example.prm391x_searchfood_vietcvfx12045.model;

public class Cart {
    private int id_cart, id_account, id_product, amount;
    private double sum;
    private int status;
    private String date_register, product_name;
    private byte [] product_image;
    private double price;
    private String unit;

    public Cart(int id_cart, int id_account, int id_product, int amount, double sum, int status, String date_register, String product_name, byte[] product_image, double price, String unit) {
        this.id_cart = id_cart;
        this.id_account = id_account;
        this.id_product = id_product;
        this.amount = amount;
        this.sum = sum;
        this.status = status;
        this.date_register = date_register;
        this.product_name = product_name;
        this.product_image = product_image;
        this.price = price;
        this.unit = unit;
    }

    public int getId_cart() {
        return id_cart;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
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
}
