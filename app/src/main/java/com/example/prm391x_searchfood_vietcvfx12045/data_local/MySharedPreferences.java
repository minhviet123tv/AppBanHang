package com.example.prm391x_searchfood_vietcvfx12045.data_local;

import android.content.Context;
import android.content.SharedPreferences;

/*
Đường ống dẫn dữ liệu | Đầu nhận là SharedPreferences của máy

- Class put dữ liệu vào và get dữ liệu ra vào SharedPreferences của máy
- Là một class thông thường nhưng tác động vào bộ nhớ của máy
- Tách riêng vì có thể coi là có một SharedPreferences cho một máy
 */
public class MySharedPreferences {
    //Tên key (Thư mục) lưu dữ liệu tự đặt | Giống tên CSDL
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context context;


    public MySharedPreferences(Context context) {
        this.context = context;
    }

     //I. Hàm lưu dữ liệu String | Sử dụng Gson (implementation 'com.google.code.gson:gson:2.10.1') để lưu object
    public void putStringValue(String key, String value){
        //Khai báo SharedPreferences: Tên Thư mục, MODE_PRIVATE
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //tính năng chỉnh sửa dữ liệu

        //Thêm dữ liệu
        editor.putString(key, value);
        editor.apply(); //thực hiện (áp dụng)
    }

    //II. Hàm xoá dữ liệu: theo key
    public void deleteStringValue(String key){
        //Khai báo SharedPreferences: Tên Thư mục, MODE_PRIVATE
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //tính năng chỉnh sửa dữ liệu

        //Xoá dữ liệu
        editor.remove(key);
        editor.apply(); //thực hiện (áp dụng)
    }

    //III. Hàm get dữ liệu String ra: Lấy theo key
    public String getStringValue(String key){
        //Khai báo SharedPreferences: Tên Thư mục, MODE_PRIVATE
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        //Trả về giá trị theo key, giá trị mặt định nếu không get được
        return sharedPreferences.getString(key, "");
    }



}
