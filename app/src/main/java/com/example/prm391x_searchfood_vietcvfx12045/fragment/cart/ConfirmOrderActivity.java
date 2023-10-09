package com.example.prm391x_searchfood_vietcvfx12045.fragment.cart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.cart.CartFragment;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;

public class ConfirmOrderActivity extends AppCompatActivity {
    private TextView txt_email_confirm, txt_phone_confirm, txt_address_confirm, txt_action_bar;
    private Button btn_confirm_order, btn_cancel_confirm_order;
    private ImageView img_back_action_bar;
    private Account account;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        //1. Ánh xạ
        AnhXa();

        //2. Gán giá trị
        txt_email_confirm.setText(account.getEmail());
        txt_phone_confirm.setText(account.getPhone());
        txt_address_confirm.setText(account.getAddress());
        txt_action_bar.setText(R.string.title_confirm_order);

        //3.1 Back trên action bar
        img_back_action_bar.setOnClickListener(v -> {
            onBackPressed();
        });
        //3.2 Sự kiện huỷ -> Back về trước đó
        btn_cancel_confirm_order.setOnClickListener(v -> {
            onBackPressed();
        });

        //4. Sự kiện đặt mua (điền thông tin vào CSDL) -> Gọi trực tiếp hàm từ CartFragment (Fragment vẫn đang hoạt động theo vòng đời)
        btn_confirm_order.setOnClickListener(v -> {
            try {
                CartFragment.Instances().Order(); //Phương án khác: truyền dữ liệu từ activity về Fragment (tại hàm onResume của Fragment) để xác nhận thực hiện hàm Order() (Sau khi bấm đặt hàng cần back lại ngay)
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Toast.makeText(this, "Đặt mua thành công! \nXem lại tại MyOrder!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        });
    }

    //1. Ánh xạ
    private void AnhXa() {
        txt_email_confirm = findViewById(R.id.txt_email_confirm);
        txt_phone_confirm = findViewById(R.id.txt_phone_confirm);
        txt_address_confirm = findViewById(R.id.txt_address_confirm);
        btn_confirm_order = findViewById(R.id.button_confirm_order);
        btn_cancel_confirm_order = findViewById(R.id.button_cancel_confirm_order);

        txt_action_bar = findViewById(R.id.textView_action_bar);
        img_back_action_bar = findViewById(R.id.imageView_back_action_bar);

        account = account = DataLocalManager.getAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        account = DataLocalManager.getAccount();
    }
}