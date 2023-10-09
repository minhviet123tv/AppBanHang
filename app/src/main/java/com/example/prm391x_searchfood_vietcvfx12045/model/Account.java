package com.example.prm391x_searchfood_vietcvfx12045.model;

import java.io.Serializable;

public class Account implements Serializable {
    private int id_account;
    private String email, password, phone, address, register_date;

    public Account() {
    }

    public Account(int id_account, String email, String password, String phone, String address, String register_date) {
        this.id_account = id_account;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.register_date = register_date;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

}
