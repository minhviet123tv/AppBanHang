package com.example.prm391x_searchfood_vietcvfx12045.data_local;

import android.app.Application;

import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite.FavoriteFragment;

/*
Khởi tạo cơ chế truyền dữ liệu (Class tự động Khởi tạo những thứ sử dụng trong phạm vi App)
Chú ý: khai báo class này trong name trong Manifests
 */
public class MyApplication extends Application {

    //Hàm khởi tạo (sẵn) data local (máy cài app)
    @Override
    public void onCreate() {
        super.onCreate();

        DataLocalManager.init(getApplicationContext());

    }
}
