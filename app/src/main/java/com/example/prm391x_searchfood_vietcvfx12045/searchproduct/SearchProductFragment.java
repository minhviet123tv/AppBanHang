package com.example.prm391x_searchfood_vietcvfx12045.searchproduct;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product.ProductAdapter;

import java.sql.SQLException;

public class SearchProductFragment extends Fragment {
    private SearchView sv_product;
    private View view;

    public SearchProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_product, container, false);

        //1. Ánh xạ
        sv_product = view.findViewById(R.id.searchView_product_fragment);

        //2. Sự kiện của SearchView (khi submit) -> Mở activity search, truyền query (key search)
        SearchKey();




        return view;
    }



    //1. Sự kiện của SearchView (khi submit) -> Mở activity search, truyền query (key search)
    private void SearchKey() {

        sv_product.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Sự kiện khi submit key search -> Mở activity search, truyền query (key search)
                Intent intent = new Intent(getActivity(), SearchProductActivity.class);
                intent.putExtra("SearchKey", query);
                startActivity(intent);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Sự kiện khi text có thay đổi
                return false;
            }
        });


        //6.2 sự kiện khi focus thay đổi (hasFocus true hoặc false)
//        sv_product.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    Intent intent = new Intent(getActivity(), SearchProductActivity.class);
//                    startActivity(intent);
//                }
//
//            }
//        });
    }

    //2. Load mỗi khi về trang
    @Override
    public void onResume() {
        super.onResume();

        //load lại trạng thái searchView mỗi lần về trang
//      sv_product.onActionViewCollapsed(); //Đóng hẳn search view lại, không select hay focus (Nhưng có vẻ phím kính lúp sẽ thay bằng mũi tên)
        sv_product.clearFocus(); //out focus
        sv_product.setQuery(null, false); //set giá trị query
    }
}