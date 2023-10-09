package com.example.prm391x_searchfood_vietcvfx12045.model;

import java.io.Serializable;

public class MyOrder implements Serializable {

    //Khai báo đúng thứ tự để hàm tạo tạo thứ tự như CSDL
    private int id_order, id_account;
    private String phone, address;
    private byte [] img_product_largest_sum;
    private double total_money;
    private String order_date, wait_ship_date, shipping_date, receive_date, cancel_order_date, return_order_date;
    private int status;

    public MyOrder(int id_order, int id_account, String phone, String address, byte[] img_product_largest_sum, double total_money, String order_date, String wait_ship_date, String shipping_date, String receive_date, String cancel_order_date, String return_order_date, int status) {
        this.id_order = id_order;
        this.id_account = id_account;
        this.phone = phone;
        this.address = address;
        this.img_product_largest_sum = img_product_largest_sum;
        this.total_money = total_money;
        this.order_date = order_date;
        this.wait_ship_date = wait_ship_date;
        this.shipping_date = shipping_date;
        this.receive_date = receive_date;
        this.cancel_order_date = cancel_order_date;
        this.return_order_date = return_order_date;
        this.status = status;
    }

    //Hàm khởi tạo dùng khi mới Order
    public MyOrder(int id_account, String phone, String address, byte[] img_product_largest_sum, double total_money) {
        this.id_account = id_account;
        this.phone = phone;
        this.address = address;
        this.img_product_largest_sum = img_product_largest_sum;
        this.total_money = total_money;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getImg_product_largest_sum() {
        return img_product_largest_sum;
    }

    public void setImg_product_largest_sum(byte[] img_product_largest_sum) {
        this.img_product_largest_sum = img_product_largest_sum;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getWait_ship_date() {
        return wait_ship_date;
    }

    public void setWait_ship_date(String wait_ship_date) {
        this.wait_ship_date = wait_ship_date;
    }

    public String getShipping_date() {
        return shipping_date;
    }

    public void setShipping_date(String shipping_date) {
        this.shipping_date = shipping_date;
    }

    public String getReceive_date() {
        return receive_date;
    }

    public void setReceive_date(String receive_date) {
        this.receive_date = receive_date;
    }

    public String getCancel_order_date() {
        return cancel_order_date;
    }

    public void setCancel_order_date(String cancel_order_date) {
        this.cancel_order_date = cancel_order_date;
    }

    public String getReturn_order_date() {
        return return_order_date;
    }

    public void setReturn_order_date(String return_order_date) {
        this.return_order_date = return_order_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
