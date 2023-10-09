package com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.MySharedPreferences;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private ImageView img_back;
    private RecyclerView rcv_favorite_list;
    private List<Favorite> favoriteList;
    private FavoriteAdapter favoriteAdapter;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private Account account = DataLocalManager.getAccount();
    private TextView txt_notify;
    private Button btn_allToCart, btn_clear;

    private View view;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    //I* Hàm khởi tạo
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);

        //1.Ánh xạ, khai báo
        AnhXa();

        //2.1 Tạo adapter và set rcv
        loadRecyclerViewFavorite();
        //2.2 Set đường thẳng cuối dòng ngăn cách mỗi item của rcv (Nên set tại đây để không bị chồng thêm)
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcv_favorite_list.addItemDecoration(itemDecoration);

        //3. Sự kiện back
        img_back.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setFragment(5);
        });

        //4. Clear favorite
        btn_clear.setOnClickListener(v -> {

            loadRecyclerViewFavorite();

            //Kiểm tra nếu chưa có hàng trong cart
            if(favoriteList.size() == 0){
                Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                //Update toàn bộ trạng thái status cho một account sang 2 (tự đặt là xoá)
                truyVanSanPham.updateAllStatusFavorite(account.getId_account(), 2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            //Cập nhật lại adapter
            loadRecyclerViewFavorite();

        });

        //5. Add all favorite to Cart
        btn_allToCart.setOnClickListener(v -> {
            loadRecyclerViewFavorite();

            //Kiểm tra nếu chưa có hàng trong cart
            if(favoriteList.size() == 0){
                Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
                return;
            }

            //Tạo hàm thêm list (Thêm từng product) vào Cart trong CSDL
            for(int i=0; i<favoriteList.size(); i++){
                try {
                    truyVanSanPham.InsertProductToCart(account.getId_account(), favoriteList.get(i).getId_product(), 1, favoriteList.get(i).getProduct_price());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            //Update toàn bộ trạng thái status của favorite cho một account sang 2 (xoá)
            try {
                truyVanSanPham.updateAllStatusFavorite(account.getId_account(), 2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Toast.makeText(getContext(), "Hoàn thành!", Toast.LENGTH_SHORT).show();

            //Load lại rcv
            loadRecyclerViewFavorite();

        });

        return view;
    }

    //I. Ánh xạ
    private void AnhXa() {
        rcv_favorite_list = view.findViewById(R.id.recyclerView_favorite_list);
        favoriteList = new ArrayList<>(); //Phải khai báo list mới có thể dùng và gọi hàm load được
        txt_notify = view.findViewById(R.id.textView_notify_favorite_fragment);
        img_back = view.findViewById(R.id.imageView_back_favorite);
        btn_allToCart = view.findViewById(R.id.button_favorite_all_to_cart);
        btn_clear = view.findViewById(R.id.button_favorite_clear);
    }

    //II* Load lại rcv khi mở activity hay fragment khác và mở lại hoặc quay trở lại (theo vòng đời fragment)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        loadRecyclerViewFavorite(); //Gọi hàm load đã tạo
    }

    //II. Load rcv
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadRecyclerViewFavorite(){

        //1. Clear và lấy lại list từ CSDL
        favoriteList.clear();

        try {
            favoriteList = truyVanSanPham.getListFavorite(DataLocalManager.getAccount().getId_account(), 1); //Trạng thái 1 là yêu thích
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //2. Hiện hoặc ẩn thông báo theo tình trạng của List (không có phần tử và có phần tử; không có tình trạng null)
        if(favoriteList.size() == 0){
            txt_notify.setVisibility(View.VISIBLE);
            return;
        }

        txt_notify.setVisibility(View.GONE);

        //3. Cài đặt lại adapter và rcv
        favoriteAdapter = new FavoriteAdapter(favoriteList, favorite -> {

            //Thêm product vào danh sách Cart
            double sum = favorite.getProduct_price(); //tổng 1 lần thêm chỉ bằng giá đơn 1 product

            try {
                truyVanSanPham.InsertProductToCart(account.getId_account(), favorite.getId_product(), 1, sum);
                Toast.makeText(getContext(), "Đã thêm: " + favorite.getProduct_name(), Toast.LENGTH_SHORT).show();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }, id_product -> {
            //Mở thông tin chi tiết product
            Intent intent = new Intent(getActivity(), DetailsProductActivity.class);
            intent.putExtra("id_product", id_product);
            startActivity(intent);

        }, favorite -> {
            //Xoá favorite (status -> 2) trong CSDL
            try {
                truyVanSanPham.updateStatusFavorite(account.getId_account(), favorite.getId_product(), 2);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

//            loadRecyclerViewFavorite(); //Cập nhật lại rcv để trường hợp xoá hết thì hiện luôn notify nếu muốn
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcv_favorite_list.setLayoutManager(linearLayoutManager);
        rcv_favorite_list.setAdapter(favoriteAdapter);

    }
}