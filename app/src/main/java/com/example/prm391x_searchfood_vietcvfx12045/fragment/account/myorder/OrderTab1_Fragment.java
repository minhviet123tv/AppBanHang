package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.AccountFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.MyOrderFragment;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderTab1_Fragment extends Fragment {

    private View view;
    private RecyclerView rcv_order_list;
    private List<MyOrder> myOrderList;
    private MyOrderAdapter myOrderAdapter;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private Account account;
    private int status; //Sử dụng để lấy list từ CSDL, giá trị dựa vào hàm khởi tạo

    //Hàm khởi tạo fragment chứa tham số
    public OrderTab1_Fragment(int status) {
        // Required empty public constructor
        this.status = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_tab_1, container, false);

        //1. Ánh xạ
        AnhXa();
        account = DataLocalManager.getAccount();
        myOrderList = new ArrayList<>();

        //2. Khai báo list, trạng thái 1 là "Đã đặt mua"
        try {
            myOrderList = truyVanSanPham.getListMyOrder(account.getId_account(), status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //3. Khai báo adapter
        myOrderAdapter = new MyOrderAdapter(myOrderList, myOrder -> {

            //Truyền dữ liệu là đối tượng MyOrder đến fragment khác
            MyOrderDetailsFragment myOrderDetailsFragment = new MyOrderDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MyOrder", myOrder);
            myOrderDetailsFragment.setArguments(bundle); //set dữ liệu cho fragment được truyền trước khi chuyển

            //Chuyển fragment
            AccountFragment.Instances().setFragmentChildren(myOrderDetailsFragment);
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_order_list.setLayoutManager(linearLayoutManager);
        rcv_order_list.setAdapter(myOrderAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcv_order_list.addItemDecoration(itemDecoration);


        return view;
    }

    //Load dữ liệu mỗi khi vào trang
    @Override
    public void onResume() {
        super.onResume();

        //Khai báo account
        account = DataLocalManager.getAccount();

        //Khai báo list, trạng thái 1 là "Đã đặt mua"
        try {
            myOrderList = truyVanSanPham.getListMyOrder(account.getId_account(), 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //1. Ánh xạ
    private void AnhXa() {
        rcv_order_list = view.findViewById(R.id.rcv_order_list);
    }
}