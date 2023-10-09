package com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.List;

public class Tab8ProductFragment extends Fragment {
    private RecyclerView rcv_product_list;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private ProgressBar pb_load_tab_product;

    public Tab8ProductFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_list_product, container, false);

        //1. Ánh xạ, khai báo
        rcv_product_list = view.findViewById(R.id.recyclerView_product_list1);
        pb_load_tab_product = view.findViewById(R.id.progress_load_tab_product);

        //2. Khai báo adapter, sự kiện 1 click ảnh -> Chuyển sang màn hình details | Sự kiện 2: Add (hoặc insert vào Cart)
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                productList = truyVanSanPham.getListProduct(8);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            setAdapterAndRecyclerView();
        }, 1000);


        return view;
    }

    //2. Khai báo adapter, sự kiện 1 click ảnh -> Chuyển sang màn hình details | Sự kiện 2: Add (hoặc insert vào Cart)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAdapterAndRecyclerView() {

        productAdapter = new ProductAdapter(productList, product -> {
            //Chuyển sang màn hình details
            Intent intent = new Intent(getActivity(), DetailsProductActivity.class);
            intent.putExtra("id_product", product.getId_product());
            startActivity(intent);

        }, product -> {
            //Thêm một đơn vị sản phẩm vào giỏ hàng
            int id_account = DataLocalManager.getAccount().getId_account();
            double sum = product.getPrice(); //tổng 1 lần thêm chỉ bằng giá đơn 1 product

            try {
                truyVanSanPham.InsertProductToCart(id_account, product.getId_product(), 1, sum);
                Toast.makeText(getContext(), "Đã thêm: " + product.getProduct_name(), Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcv_product_list.setLayoutManager(gridLayoutManager);
        rcv_product_list.setAdapter(productAdapter);

        //Ẩn progress bar sau khi load xong fragment
        pb_load_tab_product.setVisibility(View.GONE);
    }
}