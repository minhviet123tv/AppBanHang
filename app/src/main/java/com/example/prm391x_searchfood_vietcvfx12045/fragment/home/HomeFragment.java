package com.example.prm391x_searchfood_vietcvfx12045.fragment.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product.TabProductFragment;
import com.example.prm391x_searchfood_vietcvfx12045.searchproduct.SearchProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product.ProductAdapter;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TextView txtSeeAll_categories, txtSeeAll_popular;
    private View view;
    private RecyclerView rcv_categories, rcv_popular_home;
    private CategoriesAdapter adapterCategories;
    private ProductAdapter adapterProduct;
    private List<Product> productListView;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    public HomeFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        //1. Ánh xạ, khai báo
        AnhXa();

        //2. Khai báo CategoriesAdapter, sự kiện click item
        try {
            adapterCategories = new CategoriesAdapter(truyVanSanPham.getListCategories(), categories -> {
                //Truyền dữ liệu từ fragment này đi (nhận tại TabProductFragment để mở đúng tab categries này) | Đây là cách truyền dữ liệu giữa các Fragment (không nằm cùng 1 màn hình, nếu truyền xong rồi transaction có thể dùng setArguments)
                Bundle bundle = new Bundle();
                bundle.putInt("id_categories",categories.getId_categories());
                getParentFragmentManager().setFragmentResult("DataSelectCategories", bundle);

                ((MainActivity) getActivity()).setFragment(5);
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //3. Dạng hiển thị cho recyclerView: Màn hình, hướng, false
        LinearLayoutManager linearLayoutManager1 =new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcv_categories.setLayoutManager(linearLayoutManager1);
        rcv_categories.setAdapter(adapterCategories);

        //4. Chuyển sang fragment tương ứng khi click see all | Dùng hàm từ Main
        txtSeeAll_categories.setOnClickListener(v -> {
            //Gọi trực tiếp hàm từ Main để chuyển sang fragment 1 (main là 0)
            ((MainActivity) getActivity()).setFragment(1);
        });

        txtSeeAll_popular.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setFragment(5);
        });

        //5.Lấy list product theo views từ CSDL -> Khai báo adapter cho rcv card sản phẩm (lấy danh sách sản phẩm theo views)
        //Có thể thêm các cột như: add_to_cart, add_favorite cho Product trong CSDL để có thêm các thống kê (mỗi lần add to cart hoặc add to favorite thì bộ đếm của sản phẩm sẽ tăng lên) -> Dùng giới thiệu hiện sản phẩm theo các kiểu: xem, yêu thích, chọn, đặt mua, huỷ mua
        //Hoặc cũng có thể tạo thêm bảng views riêng cho từng account gồm các cột như: id_table, id_account, id_product, views, add_to_cart, remove_cart, add_favorite, remove_favorite ... -> Để giới thiệu sản phẩm liên quan, đã từng xem hay chọn nhiều, sản phẩm mới chưa xem đối với cá nhân, sản phẩm được đặt mua nhiều nhất
        loadRecyclerViewProduct();




        return view;
    }

    //I. Ánh xạ
    private void AnhXa() {
        txtSeeAll_categories = view.findViewById(R.id.textView_see_all_catagory);
        txtSeeAll_popular = view.findViewById(R.id.textView_see_all_popular_home);
        rcv_categories = view.findViewById(R.id.recyclerView_categories_home);
        rcv_popular_home = view.findViewById(R.id.recyclerView_popular_home);
        productListView = new ArrayList<>();
    }

    //II. Hàm load rcv
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadRecyclerViewProduct() {
        //1. Clear list và khai báo lại list
        productListView.clear();

        try {
            productListView = truyVanSanPham.getListProductViews();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //2. Khai báo adapter và 2 sự kiện click (ảnh và dấu + vào giỏ hàng)
        adapterProduct = new ProductAdapter(productListView, product -> {
            //Mở màn hình chi tiết details
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

        LinearLayoutManager linearLayoutManager2 =new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rcv_popular_home.setLayoutManager(linearLayoutManager2);
        rcv_popular_home.setAdapter(adapterProduct);
    }

    //III* Load lại rcv product nếu cần | Đặt lại tình trạng cho searchView
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
//        loadRecyclerViewProduct();

        //set phím back cứng (nếu ở activity) để thoát khỏi search
//        if(sv_product.isIconified()){
//            sv_product.setIconified(true);
//        }
    }

}