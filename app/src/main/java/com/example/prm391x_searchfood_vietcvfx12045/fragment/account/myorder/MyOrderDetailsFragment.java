package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.DetailsProductActivity;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.AccountFragment;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrder;
import com.example.prm391x_searchfood_vietcvfx12045.model.MyOrderDetails;

import java.sql.SQLException;
import java.util.List;


public class MyOrderDetailsFragment extends Fragment {
    private TextView txt_number_myorder, txt_total_money;
    private RecyclerView rcv_myorder_details_list;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private List<MyOrderDetails> myOrderDetailsList;
    private MyOrderDetailsAdapter myOrderDetailsAdapter;
    private MyOrder myOrder;
    private View view;

    public MyOrderDetailsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_order_details, container, false);

        //1. Ánh xạ
        AnhXa();
        AccountFragment.Instances().setBack(1); // Hiện phím back

        //2. Lấy dữ liệu MyOrder được truyền đến
        Bundle bundle = getArguments();

        if(bundle != null){
            myOrder = (MyOrder) bundle.getSerializable("MyOrder");
        }

        //3. Gán các giá trị cho activity
        txt_number_myorder.setText("Chi tết đơn hàng: #" + myOrder.getId_order());
        txt_total_money.setText("$" + myOrder.getTotal_money());


        //5. Lấy danh sách sản phẩm theo id_order truyền đến
        try {
            myOrderDetailsList = truyVanSanPham.getListMyOrderDetails(myOrder.getId_order());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //6. Tạo adapter cho rcv: List, sự kiện click ảnh mở chi tiết product
        myOrderDetailsAdapter = new MyOrderDetailsAdapter(myOrderDetailsList, myOrderDetails -> {

            //Set dữ liệu trước khi truyền
            ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putInt("id_product" ,myOrderDetails.getId_product());
            productDetailsFragment.setArguments(bundle2);

            //Chuyển fragment
            AccountFragment.Instances().setFragmentChildren(productDetailsFragment);
        });

        //Định dạng kiểu hiển thị cho rcv
        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_myorder_details_list.setLayoutManager(linearLayoutManager);
        rcv_myorder_details_list.setAdapter(myOrderDetailsAdapter);

        //Tạo line giữa các dòng
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcv_myorder_details_list.addItemDecoration(itemDecoration);

        return view;
    }

    private void AnhXa() {
        txt_number_myorder = view.findViewById(R.id.textView_number_myorder);
        rcv_myorder_details_list = view.findViewById(R.id.rcv_myorder_details_list);
        txt_total_money = view.findViewById(R.id.txt_number_total_money_myorder_details);
    }
}