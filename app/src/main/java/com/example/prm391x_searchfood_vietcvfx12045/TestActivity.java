package com.example.prm391x_searchfood_vietcvfx12045;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextView txt_email;
    private final int REQUEST_CODE_FOLDER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imgAvatar = findViewById(R.id.imageView_account_avatar_test);
        txt_email = findViewById(R.id.textView_account_email_test);

        imgAvatar.setOnClickListener(v -> {
            //Hỏi quyền người dùng để truy cập folder bộ nhớ máy: Activity, permission, request code
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER );
        });


    }

    //II. Xử lý sau xem (trả lời) quyền truy cập (camera và bộ nhớ folder) -> Truy cập dữ liệu (2*)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_FOLDER){

            //Nếu có trả lời (>0) và đồng ý: Tạo intent implicit mở FOLDER trả về hàm xử lý Result theo request code
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Tạo Intent mở máy lấy hình, file | Chọn kiểu dữ liệu (thư mục, nơi chứa sẽ lấy) hình | Run trả về kết quả theo code
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivity(intent);

                //Nếu không đồng ý -> thông báo
            } else {
                Toast.makeText(this, "Vui lòng cấp quyền tại cài đặt!", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}