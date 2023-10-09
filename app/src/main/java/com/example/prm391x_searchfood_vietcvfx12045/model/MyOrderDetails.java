package com.example.prm391x_searchfood_vietcvfx12045.model;

public class MyOrderDetails {
    private int id_order, id_product, amount;
    private double price, sum;

    public MyOrderDetails(int id_order, int id_product, int amount, double price, double sum) {
        this.id_order = id_order;
        this.id_product = id_product;
        this.amount = amount;
        this.price = price;
        this.sum = sum;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
