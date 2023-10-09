package com.example.prm391x_searchfood_vietcvfx12045.searchproduct;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchProductActivity extends AppCompatActivity {
    private ImageView img_back_action_bar;
    private TextView txt_action_bar;
    private SearchView sv_product_activity;
    private RecyclerView rcv_search_product;
    private List<Product> productList;
    private SearchProductAdapter searchProductAdapter;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private Account account = DataLocalManager.getAccount();
    private ProgressBar pb_load_search_product;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        //1. Ánh xạ
        AnhXa();

        //2.1 Lấy dữ liệu truyền đến
        Intent intent = getIntent();
        String searchKey = intent.getStringExtra("SearchKey");

        //2.2 Mở sẵn sv, set giá trị query cho searchView, ẩn bàn phím
        sv_product_activity.onActionViewExpanded();
        sv_product_activity.setQuery(searchKey, false);

        //3.1 Load rcv, tìm kiếm luôn từ đã gửi lên. Sử dụng Handler để hiện load trước khi lấy được dữ liệu lên để hiển thị
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                LoadSearchProductRecyclerView(searchKey);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }, 100);

        //3.2 Thêm line giữa các dòng
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcv_search_product.addItemDecoration(itemDecoration);

        //4. Dùng Filter để lọc tìm kiếm theo searchKey
        MakeFilterOfRecyclerView();

        //5. Hàm back
        img_back_action_bar.setOnClickListener(v -> {
            onBackPressed();
        });

    }


    //1. Ánh xạ
    @SuppressLint("SetTextI18n")
    private void AnhXa() {
        sv_product_activity = findViewById(R.id.searchView_product_activity);
        img_back_action_bar = findViewById(R.id.imageView_back_action_bar);
        txt_action_bar = findViewById(R.id.textView_action_bar);
        rcv_search_product = findViewById(R.id.rcv_search_product);

        txt_action_bar.setText("Search");
        productList = new ArrayList<>();

        pb_load_search_product = findViewById(R.id.progress_load_search_product);
    }

    //2. Load rcv
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void LoadSearchProductRecyclerView(String searchKey) throws SQLException {

        //a. Làm mới mỗi lần gọi
        productList.clear();
        //b. Lấy list từ CSDL
        productList = truyVanSanPham.getListAllProduct();

        //c. Khai báo adapter và các sự kiện
        searchProductAdapter = new SearchProductAdapter(productList, product -> {

            //Thêm product vào cart
            truyVanSanPham.InsertProductToCart(account.getId_account(), product.getId_product(), 1, product.getPrice());
            Toast.makeText(this, "Đã thêm: " + product.getProduct_name(), Toast.LENGTH_SHORT).show();

        }, product -> {

            //Mở màn hình chi tiết details
            Intent intent = new Intent(this, DetailsProductActivity.class);
            intent.putExtra("id_product", product.getId_product());
            startActivity(intent);
        });

        //d. Set kiểu hiển thị và adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcv_search_product.setLayoutManager(linearLayoutManager);
        rcv_search_product.setAdapter(searchProductAdapter);

        //e. Search trước từ khoá đã gửi lên
        searchProductAdapter.getFilter().filter(searchKey);

        //f. Tắt progress bar sau khi load xong
        pb_load_search_product.setVisibility(View.GONE);

    }

    //3* Load mỗi khi vào trang
    @Override
    protected void onResume() {
        super.onResume();

        //out focus
        sv_product_activity.clearFocus();

        //Ẩn bàn phím
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    //4. Dùng Filter để lọc tìm kiếm theo searchKey
    private void MakeFilterOfRecyclerView() {

        sv_product_activity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //Gọi Filter của adapter
                searchProductAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProductAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}