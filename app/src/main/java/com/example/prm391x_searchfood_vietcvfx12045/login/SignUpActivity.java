package com.example.prm391x_searchfood_vietcvfx12045.login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SignUpActivity extends AppCompatActivity {
    ImageView imgBack, imgBanner_sign_up;
    EditText edtEmail, edtPassword, edtConfirmPassword;
    TextView txtNotification;
    Button btnNext;
    TruyVanAccountMSSQL truyVan = new TruyVanAccountMSSQL();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //1.Ánh xạ
        AnhXa();

        //Set tạm giá trị
        edtEmail.setText("minhviet2.dragon@gmail.com");
        edtPassword.setText("123456789");
        edtConfirmPassword.setText("123456789");

        //2. Back lại Login Activity
        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //4. Sự kiện next -> Xử lý dữ liệu đã điền vào
        btnNext.setOnClickListener(v -> {

            //a. Kiểm tra dữ liệu nhập. Lấy giá trị cuối của dữ liệu nhập vào
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            //Ẩn thông báo, khi nào có thông báo mới set lên
            txtNotification.setVisibility(View.GONE);

            //Kiểm tra và thông báo nếu có giá trị trống
            if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
                txtNotification.setText(R.string.empty_edit_text_notification);
                txtNotification.setVisibility(View.VISIBLE);
                return;

            //Thông báo nếu mật khẩu không trùng nhau
            } else if (!password.equals(confirmPassword)){
                txtNotification.setText(R.string.mat_khau_khong_trung_nhau);
                txtNotification.setVisibility(View.VISIBLE);
                return;
            }

            //b. Kiểm tra cú pháp email, password tài khoản
            User user = new User(email, password);

            if(!user.kiemtra()){
                txtNotification.setText(user.getMessage());
                txtNotification.setVisibility(View.VISIBLE);
                return;

            //c. Kiểm tra tồn tại email | Thêm email vào CSDL
            } else {

                try {
                    if (truyVan.checkAccount(email)) {
                        txtNotification.setText(R.string.tai_khoan_da_ton_tai);
                        txtNotification.setVisibility(View.VISIBLE);
                        return;

                    //Nếu chưa tồn tại thì thêm vào CSDL
                    } else {

                        //Lấy thời gian chính xác đến giây (ss) để làm thời gian đăng ký tài khoản (Sẽ được lấy chính xác mỗi lần chạy đến đây)
                        LocalDateTime myDateTime = LocalDateTime.now();
                        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String timeNow = myDateTime.format(myFormat);

                        //Thêm tài khoản vào CSDL
                        truyVan.InsertAccount(email, password, timeNow);
                        Toast.makeText(this, R.string.dang_ky_thanh_cong, Toast.LENGTH_SHORT).show();

                        //Chuyển sang màn hình login
                        Intent intentSignUp = new Intent(SignUpActivity.this, SignInActivity.class);
                        intentSignUp.putExtra("email", email);
                        intentSignUp.putExtra("password", password);
                        startActivity(intentSignUp);
                    }

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    //I. Ánh xạ
    private void AnhXa() {
        imgBack = findViewById(R.id.imageView_icon_back_sign_up);
        imgBanner_sign_up = findViewById(R.id.imageView_banner_signup);
        edtEmail = findViewById(R.id.editText_email_sign_up);
        edtPassword = findViewById(R.id.editText_password_sign_up);
        edtConfirmPassword = findViewById(R.id.editText_confirm_password_sign_up);
        txtNotification = findViewById(R.id.textView_notification_sign_up);
        btnNext = findViewById(R.id.buttonNext_sign_up);
    }
}