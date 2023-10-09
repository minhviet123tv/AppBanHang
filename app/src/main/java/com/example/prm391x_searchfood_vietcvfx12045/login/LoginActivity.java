package com.example.prm391x_searchfood_vietcvfx12045.login;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;
import java.util.Objects;

/*
SharedPreferences: Có chức năng lưu vừa gần giống session của java web (sử dụng cặp key + value)
Vừa giống SQLite là lưu giá trị dữ liệu vào app máy (app được cài trong máy)
MySharedPreferences: Tạo kiểu lưu, get dữ liệu | DataLocalManager: Lưu dữ liệu cụ thể theo key tự tạo

Chú ý: Tên dữ liệu, tên key nên đặt riêng biệt để tránh hack lấy dữ liệu theo tên phổ thông (Nhưng nếu dữ liệu chỉ cài trong app máy thì có thể không sao)
 */
public class LoginActivity extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    TruyVanAccountMSSQL truyVan = new TruyVanAccountMSSQL();

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Chú ý set Status bar | Chế độ trong themes.xml

        //1. Ánh xạ
        btnSignUp = findViewById(R.id.buttonSignUp);
        btnSignIn = findViewById(R.id.buttonSignIn);

        //Xoá tài khoản lưu local -> Sử dụng khi Logout | Để tạm đây để code
//        DataLocalManager.deleteAccount();

        //2. Nếu có Account lưu local và khớp email, password trong CSDL thì chuyển vào main  (lưu sẵn thông tin đăng nhập trong máy, lấy ra để kiểm tra và sử dụng)
        if(DataLocalManager.getAccount() != null) {

            try {
                //Vẫn truy vấn kiểm tra mà không chuyển luôn để trường hợp không có mạng internet thì không đăng nhập được | Có thể bỏ kiểm tra để lưu local -> khi nào có mạng thì up
                if (truyVan.checkEmailPassword(DataLocalManager.getAccount().getEmail(), DataLocalManager.getAccount().getPassword())) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //3.Nếu không có tài khoản, hoặc không khớp -> Sự kiện chuyển sang màn hình tương ứng
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);

        });

        btnSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            intent.putExtra("email", "minhviet15.dragon@gmail.com"); //set tạm để tiện đăng nhập code
            intent.putExtra("password", "123456789");
            startActivity(intent);
        });
    }
}