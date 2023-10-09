package com.example.prm391x_searchfood_vietcvfx12045.data_local;

import android.content.Context;

import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.google.gson.Gson;

/*
Đầu vào của dữ liệu

Class tạo hàm lưu dữ liệu gì vào máy (Các hàm sử dụng MySharedPreferences với tên key cụ thể "PREF_FIRST_INSTALL")
Khai báo static cho hàm để sử dụng trực tiếp trong Main
 */
public class DataLocalManager {

    private static final String PREF_OBJECT_ACCOUNT = "PREF_OBJECT_ACCOUNT"; // Key lưu account. Có thể tạo key khác để lưu dữ liệu khác
    private static DataLocalManager instance; //Chứa chính nó bên trong (hàm sử dụng trực tiếp trong Main thay cho việc phải tạo mới)
    private MySharedPreferences mySharedPreferences;

    //I. Hàm khởi tạo (Được gọi tạo trong MyApplication)
    public static void init(Context context){
        instance = new DataLocalManager();
        instance.mySharedPreferences = new MySharedPreferences(context);
    }

    //II. Hàm get (tạo) đối tượng DataLocalManager
    public static DataLocalManager getInstance(){
        if (instance == null){
            instance = new DataLocalManager();
        }

        return instance;
    }

    //III. Truyền, lưu đối tượng vào app trong máy
    public static void setAccount(Account account){

        //Sử dụng Gson để convert một object -> thành string (dạng Json) -> lưu local
        Gson gson = new Gson();
        String strJsonUser = gson.toJson(account);

        //Lưu vào MySharedPreferences: Khai báo key tự tạo, giá trị của string
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(PREF_OBJECT_ACCOUNT, strJsonUser);
    }

    //IV. Xoá đối tượng vào app trong máy
    public static void deleteAccount(){
        DataLocalManager.getInstance().mySharedPreferences.deleteStringValue(PREF_OBJECT_ACCOUNT);
    }

    //V. Get đối tượng từ app trong máy (Sử dụng Gson để chuyển string (Json) về dạng đối tượng
    public static Account getAccount(){
        //Lấy string từ app máy (Theo đúng tên key)
        String strJsonAccount = DataLocalManager.getInstance().mySharedPreferences.getStringValue(PREF_OBJECT_ACCOUNT);

        //Chuyển dữ liệu về dạng Object: Truyền String (đã chuyển về dạng Json),(Class) Object muốn convert sang
        Gson gson = new Gson();
        Account account = gson.fromJson(strJsonAccount, Account.class);

        return account;
    }


}
