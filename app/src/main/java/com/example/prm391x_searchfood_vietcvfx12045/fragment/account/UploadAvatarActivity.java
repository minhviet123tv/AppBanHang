package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadAvatarActivity extends AppCompatActivity {
    private ImageButton ibtnCamera, ibtnFolder;
    private Button btnUpload, btnCancel;
    private CircleImageView imgAvatar;
    private final int REQUEST_CODE_CAMERA = 1;
    private final int REQUEST_CODE_FOLDER = 2;
    private final TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private final TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();
    private Account account = DataLocalManager.getAccount();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_avatar);

        //1. Ánh xạ
        AnhXa();

        //2. Load sẵn ảnh avartar của account
        LoadAvatar();

        //3. Hỏi quyền truy cập khi click button tương ứng
        RequestCodeOfButton();

        //4. Xử lý đưa ảnh từ imageView hiển thị vào CSDL
        UploadAvartarToSQL();

        //5. Cancel -> Không upload ảnh vào CSDL và trở về màn hình trước đó
        CancelUploadAvatar();
    }



    //I. Ánh xạ
    private void AnhXa() {
        ibtnCamera  = findViewById(R.id.imageButtonCamera);
        ibtnFolder  = findViewById(R.id.imageButtonUpload);
        btnUpload   = findViewById(R.id.buttonAddImage);
        btnCancel   = findViewById(R.id.buttonCancel);
        imgAvatar   = findViewById(R.id.imageView_picture_upload_avatar);
    }

    //II. LoadAvartar
    private void LoadAvatar() {

        try {
            byte [] imgAvatarSQL = truyVanAccount.getAvatar(account.getId_account(), 1);

            if(imgAvatarSQL != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgAvatarSQL, 0, imgAvatarSQL.length);
                imgAvatar.setImageBitmap(bitmap);

                //Nếu chưa có ảnh thì set ảnh khuôn mẫu
            } else {
                imgAvatar.setImageResource(R.drawable.image_avatar_default);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //III.1 Hỏi quyền truy cập khi click button tương ứng
    private void RequestCodeOfButton() {
        ibtnCamera.setOnClickListener(v -> {
            //Hỏi quyền người dùng để truy cập camera: Activity, permission, request code
            ActivityCompat.requestPermissions(UploadAvatarActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA );
        });

        ibtnFolder.setOnClickListener(v -> {
            //Hỏi quyền người dùng để truy cập folder bộ nhớ máy: Activity, permission, request code
            ActivityCompat.requestPermissions(UploadAvatarActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_FOLDER );
        });
    }

    //III.2 Xử lý sau xem (trả lời) quyền truy cập (camera, bộ nhớ folder) -> Truy cập dữ liệu
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            //1. Trường hợp mã code của camera
            case REQUEST_CODE_CAMERA:

                //a. Nếu có trả lời (>0) và đồng ý: Tạo intent implicit mở CAMERA trả về theo request code
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);

                //b. Nếu không đồng ý -> thông báo
                } else {
                    Toast.makeText(this, "Vui lòng cấp quyền Camera tại cài đặt!", Toast.LENGTH_SHORT).show();
                }

                break;

            //2. Trường hợp mã code của folder
           case REQUEST_CODE_FOLDER:

                //a. Nếu có trả lời (>0) và đồng ý: Tạo intent implicit mở FOLDER trả về theo request code
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    //Tạo Intent mở lấy hình, file | Chọn kiểu dữ liệu (thư mục, nơi chứa sẽ lấy) hình | Run trả về kết quả theo code
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_FOLDER);

                //b. Nếu không đồng ý -> thông báo
                } else {
                    Toast.makeText(this, "Vui lòng cấp quyền tại cài đặt!", Toast.LENGTH_SHORT).show();
                }

                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //III.3 Hàm nhận kết quả trả về của intent (implicit camera và folder)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //1. Nhận kết quả chụp camera, kiểm tra: Đúng mã requestCode, RESULT_OK mặc định của camera nếu click chọn hình, dữ liệu không trống
        if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){

            //a. Lấy hình ảnh
            Bitmap bitmap = (Bitmap) data.getExtras().get("data"); //tên mặc định data

            //b. Thay đổi kích thước ảnh (để có dung lượng nhẹ hơn, đơn vị có thể là pixel)
            Bitmap bitmapResize = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

            //c. Gán hình ảnh cho ImageView
            imgAvatar.setImageBitmap(bitmapResize);
        }

        //2. Nhận kết quả mở folder theo đúng code
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){

            //a. Lấy đường dẫn URI (nơi chứa tài nguyên)
            Uri uri = data.getData();

            try {
                //b. Tạo luồng đọc dữ liệu, đọc uri -> Lấy hình ảnh
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                //c. Thay đổi kích thước ảnh (để có dung lượng nhẹ hơn)
                Bitmap bitmapResize = Bitmap.createScaledBitmap(bitmap, 300, 300, true);

                //d. Set ảnh cho picture
                imgAvatar.setImageBitmap(bitmapResize);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //IV. UploadAvartarToSQL
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void UploadAvartarToSQL() {

        btnUpload.setOnClickListener(v -> {

            //a. Lấy dữ liệu hình ảnh và chuyển về Bitmap
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgAvatar.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            //b. Nếu không có ảnh thì thông báo và return
            if(bitmap == null){
                Toast.makeText(this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                return;
            }

            //b. Chuyển ảnh về mảng Byte
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();

            //Định dạng ảnh compress: Kiểu ảnh, chất lượng ảnh (mặc định 100, càng nhỏ càng net), kiểu chuyển (mảng byte đã tạo)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte [] imgArray = byteArray.toByteArray();

            //c. Upload ảnh vào CSDL
            int id_account = DataLocalManager.getAccount().getId_account();

            try {
                truyVanAccount.insertImageAccount(id_account, imgArray);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //d. Thông báo và trở về Main (theo hàm back của activity)
            Toast.makeText(this, "Xong!", Toast.LENGTH_SHORT).show();
            onBackPressed();

        });
    }

    //V. Cancel -> Không upload ảnh vào CSDL và trở về màn hình trước đó
    private void CancelUploadAvatar() {
        btnCancel.setOnClickListener(v -> {
            onBackPressed();
        });
    }

}