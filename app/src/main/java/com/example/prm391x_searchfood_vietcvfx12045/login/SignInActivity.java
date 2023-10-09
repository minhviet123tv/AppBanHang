package com.example.prm391x_searchfood_vietcvfx12045.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;

public class SignInActivity extends AppCompatActivity {
    ImageView imgBack;
    EditText edtEmail, edtPassword;
    TextView txtNotification;
    Button btnNext;
    TruyVanAccountMSSQL TruyVanAccount = new TruyVanAccountMSSQL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //1. Ánh xạ
        AnhXa();

        //2. Lấy dữ liệu vừa sign up truyền sang gắn cho editText
        Intent intent = getIntent();
        edtEmail.setText(intent.getStringExtra("email"));
        edtPassword.setText(intent.getStringExtra("password"));

        //3. Sự kiện back về màn hình login
        imgBack.setOnClickListener(v -> {
            Intent intentBack = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intentBack);
        });

        //4. Sự kiện đăng nhập, kiểm tra login
        btnNext.setOnClickListener(v -> {

            //a. Khai báo dữ liệu nhập
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString();

            try {
                //b. Kiểm tra email có tồn tại trong CSDL không
                if(TruyVanAccount.checkAccount(email)){

                    //Nếu khớp email, password -> Chuyển dữ liệu về main
                    if (TruyVanAccount.checkEmailPassword(email, password)){
                        //Chuyển sang màn hình Main
                        Intent intentSignIn = new Intent(SignInActivity.this, MainActivity.class);
                        intentSignIn.putExtra("emailSignIn", email);
                        intentSignIn.putExtra("passwordSignIn", password);

                        startActivity(intentSignIn);

                    //Nếu không khớp, báo sai mật khẩu
                    } else {
                        txtNotification.setText(R.string.sai_mat_khau);
                        txtNotification.setVisibility(View.VISIBLE);
                        return;
                    }

                //c. Nếu email không có trong CSDL -> Thông báo
                } else {
                    txtNotification.setText(R.string.tai_khoan_chua_dang_ky);
                    txtNotification.setVisibility(View.VISIBLE);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //I. Ánh xạ
    private void AnhXa() {
        imgBack = findViewById(R.id.imageView_back_sign_in);
        edtEmail = findViewById(R.id.editText_email_sign_in);
        edtPassword = findViewById(R.id.editText_password_sign_in);
        txtNotification = findViewById(R.id.textView_notification_sign_in);
        btnNext = findViewById(R.id.buttonNext_sign_in);
    }
}