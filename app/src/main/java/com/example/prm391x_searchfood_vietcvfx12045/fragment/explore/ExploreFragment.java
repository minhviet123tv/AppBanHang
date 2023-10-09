package com.example.prm391x_searchfood_vietcvfx12045.fragment.explore;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.home.CategoriesAdapter;


import java.sql.SQLException;

public class ExploreFragment extends Fragment {

    private RecyclerView rcv_explore;
    private CategoriesAdapter adapter;
    private final TruyVanSanPhamMSSQL truyVanSanPhamMSSQL = new TruyVanSanPhamMSSQL();

    private View view;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_explore, container, false);

        //1. Ánh xạ
        rcv_explore = view.findViewById(R.id.recyclerView_explore);

        //2. Khai báo CategoriesAdapter: list lấy từ MS SQL, sự kiện khi click vào item (sử dụng dữ liệu categories)
        try {
            adapter = new CategoriesAdapter(truyVanSanPhamMSSQL.getListCategories(), categories -> {
                //Truyền dữ liệu từ fragment này đi (nhận tại TabProductFragment để mở đúng (tab) id_categories này)
                Bundle bundle = new Bundle();
                bundle.putInt("id_categories",categories.getId_categories());
                getParentFragmentManager().setFragmentResult("DataSelectCategories", bundle);

                //Set Fragment Main về 5 (nằm ngoài menu)
                ((MainActivity) getActivity()).setFragment(5);
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Khai báo quản lý GridLayout: truyền biến môi trường (activity), số cột | setLayoutManager (dùng kiểu layout gì) cho rcv
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rcv_explore.setLayoutManager(gridLayoutManager);

        rcv_explore.setAdapter(adapter);

        return view;
    }
}