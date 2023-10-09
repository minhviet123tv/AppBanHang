package com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.google.android.material.tabs.TabLayout;
import java.sql.SQLException;

public class TabProductFragment extends Fragment {

    private ImageView img_back;
    private TabLayout tabLayoutProduct;
    private ViewPager viewPagerTabProduct;
    private TabProductViewPagerAdapter tabProductAdapter;
    private final TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private View view;

    public TabProductFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_product, container, false);

        //1. Ánh xạ
        AnhXa();

        //2. Set adapter cho ViewPager, ViewPager cho TabLayout
        setAdapterViewPagerAndTablayout();

        //3. Lấy dữ liệu đã truyền của các Fragment trước (gọi đến fragment này) -> Set về tab (theo id_categories đã truyền)
        setTabOfViewPager();

        //4. Back về Explore (Dùng hàm tạo của Main)
        img_back.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setFragment(1);
        });



        return view;
    }



    //3. setTabOfViewPager
    private void setTabOfViewPager() {
        getParentFragmentManager().setFragmentResultListener("DataSelectCategories", this, (requestKey, result) -> {
            int id_categories = result.getInt("id_categories");
            viewPagerTabProduct.setCurrentItem(id_categories - 1); //Số thứ tự Tab bé hơn id_categories 1 đơn vị (Do tab lấy từ 0, cần chú ý thứ tự id_categories)
        });
    }

    //2. Set adapter cho ViewPager, ViewPager cho TabLayout
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setAdapterViewPagerAndTablayout() {

        try {
            tabProductAdapter = new TabProductViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, truyVanSanPham.getListCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        viewPagerTabProduct.setAdapter(tabProductAdapter);
        tabLayoutProduct.setupWithViewPager(viewPagerTabProduct);
    }

    //1.Ánh xạ
    private void AnhXa() {
        img_back = view.findViewById(R.id.imageView_back_product);
        tabLayoutProduct = view.findViewById(R.id.tab_categories_name);
        viewPagerTabProduct = view.findViewById(R.id.viewPager_tab_product);
    }

    //Thiết lập tab khi trở lại fragment này (nếu có icon menu mở đến luôn)
    @Override
    public void onResume() {
        super.onResume();

        setTabOfViewPager();
    }
}