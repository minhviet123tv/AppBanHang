package com.example.prm391x_searchfood_vietcvfx12045.fragment.cart;

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
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.example.prm391x_searchfood_vietcvfx12045.model.Cart;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartFragment extends Fragment {
    private ImageView img_back_cart;
    private RecyclerView rcv_cart_list;
    private Button btn_order_cart, btn_clear_cart;
    private List<Cart> cartList;
    private CartAdapter cartAdapter;
    private TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();
    private int id_account = DataLocalManager.getAccount().getId_account(); //Theo SharedPreferences
    private Account account = DataLocalManager.getAccount();
    private TextView txt_notify, txt_total_money_cart;
    public static CartFragment cartFragment; //Được khai báo trong onCreateView | Có thể hình dung tương tự với một số như: public static double number

    private View view;
    //Trả về tham số CartFragment đã khai báo toàn cục
    public static CartFragment Instances(){
        return cartFragment;
    }

    public CartFragment() {
        // Required empty public constructor
    }

    //*I. Thực hiện hàm khởi tạo
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart, container, false);

        //1. Ánh xạ, khai báo
        AnhXa();
        cartFragment = this; //Khai báo CartFragment toàn cục chính bằng fragment này

        //2. Set đường thẳng cuối dòng ngăn cách mỗi item của rcv (Nên set tại đây để không bị chồng thêm) (rcv đã được load tại onResume)
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcv_cart_list.addItemDecoration(itemDecoration);

        //3. OrderCart -> Chuyển sang màn hình xác nhận
        btn_order_cart.setOnClickListener(v -> {
            OrderCart();
        });

        //4. Back về Explore (Dùng hàm tạo của Main)
        img_back_cart.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setFragment(5);
        });

        //5. Xoá Cart theo account (Chuyển trạng thái thành 2, xoá)
        btn_clear_cart.setOnClickListener(v -> {
            ClearCart();
        });


        return view;
    }




    //I. Ánh xạ
    private void AnhXa() {
        img_back_cart = view.findViewById(R.id.imageView_back_cart);
        rcv_cart_list = view.findViewById(R.id.rcv_cart_list);
        btn_order_cart = view.findViewById(R.id.button_order_cart);
        btn_clear_cart = view.findViewById(R.id.button_clear_cart);
        cartList = new ArrayList<>(); //Phải khai báo cartList mới có thể dùng hàm load rcv
        txt_notify = view.findViewById(R.id.textView_notify_cart_fragment);
        txt_total_money_cart = view.findViewById(R.id.textView_total_money_cart);
    }

    //*II. Load lại dữ liệu rcv khi trở lại fragment
    @Override
    public void onResume() {
        super.onResume();

        try {
            loadRecyclerViewCart();
            account = DataLocalManager.getAccount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //II. Load rcv
    public void loadRecyclerViewCart() throws SQLException {

        //1. Clear và lấy lại list
        cartList.clear();

        try {
            cartList = truyVanSanPham.getListCart(id_account, 1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //2. Hiện hoặc ẩn thông báo theo tình trạng của List (không có phần tử và có phần tử; không có tình trạng null)
        if(cartList.size() == 0){
            txt_notify.setVisibility(View.VISIBLE);
            return;
        }

        txt_notify.setVisibility(View.GONE); //phải đặt GONE ở đây để nếu có sản phẩm mới thì ẩn notify đi

        //3. Khai báo adapter, rcv
        cartAdapter = new CartAdapter(cartList, cart -> {
            //Chuyển sang màn hình chi tiết sản phẩm (Sự kiện click vào ảnh ở Adapter)
            Intent intent = new Intent(getActivity(), DetailsProductActivity.class);
            intent.putExtra("id_product", cart.getId_product());
            startActivity(intent);

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        rcv_cart_list.setLayoutManager(linearLayoutManager);
        rcv_cart_list.setAdapter(cartAdapter);

        //Gán giá trị tổng tiền
        setTotalMoneyOfCart();

    }

    //III. Order
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Order() throws SQLException {
        //Load lại rcv, account trước khi đặt hàng
        loadRecyclerViewCart();

        account = DataLocalManager.getAccount();

        //1. Lấy thông tin product mà có tổng lớn nhất trong Cart, lấy tổng của cart list (từ CSDL)
        Product productMaxSum = truyVanSanPham.getProduct(getIdProductHasMaxSumOfCart());
        double totalMoney = truyVanSanPham.getTotalMoneyCartOfAccount(account.getId_account());

        //2. Tiến hành Insert thông tin đặt hàng (vào bảng MyOrder và MyOrder_Details của CSDL)
        truyVanSanPham.InsertCartList(account.getId_account(), account.getPhone(), account.getAddress(), productMaxSum.getProduct_image(), totalMoney, cartList);
//        MyOrder myOrder = new MyOrder(account.getId_account(), account.getPhone(), account.getAddress(), productMaxSum.getProduct_image(), totalMoney); //Nếu truyền MyOrder thì có thể sử dụng đối tượng qua Intent

        //3. Chuyển trạng thái các sản phẩm trong giỏ hàng thành 3 (đã đặt hàng) -> Tương đương xoá danh sách trong giỏ hàng, chuyển sang trạng thái mới
        truyVanSanPham.UpdateStatusAllCart(account.getId_account(), 3);

        //Load lại rcv
        loadRecyclerViewCart();

        //Gán lại giá trị tổng tiền
        setTotalMoneyOfCart();

    }

    //IV. Lấy id_product có sum lớn nhất | Tìm vị trí cart có sum lớn nhất (Để lấy image của product đó làm ảnh đại diện của order) | Chú ý: Đây chỉ là danh sách Cart của một id_accout
    private int getIdProductHasMaxSumOfCart() throws SQLException {

//        loadRecyclerViewCart(); //Truy vấn lại list một lần cuối trước khi thực hiện -> Đã thực hiện ở hàm gọi Order()

        //1. Tạo một mảng riêng chứa sum trong list để tìm ra sum lớn nhất
        List<Double> sumList = new ArrayList<>();

        for(int i=0; i <cartList.size(); i++){
            sumList.add(cartList.get(i).getSum());
        }

        Collections.sort(sumList);
        double maxSum = sumList.get(sumList.size() - 1);

        //2. Tìm vị trí chứa maxSum trong cartList
        int positon = 0;

        for(int i=0; i<cartList.size(); i++){
            if(cartList.get(i).getSum() == maxSum){
                positon = i;
            }
        }

        //3. Trả về id của Product đã xác định là có sum lớn nhất trong list
        return cartList.get(positon).getId_product();
    }

    //V. Tính tổng tiền của cartList, gán giá trị hiển thị (trong mỗi lần được gọi)
    public void setTotalMoneyOfCart() throws SQLException {
        //Lấy tổng tiền trực tiếp từ CSDL
        double total_money = truyVanSanPham.getTotalMoneyCartOfAccount(account.getId_account());
        //Gán giá trị tổng tiền của Cart vào textView
        if(total_money == 0){
            txt_total_money_cart.setText("$" + 0.00);
        } else {
            txt_total_money_cart.setText("$" + total_money);
        }
    }

    //VI. OrderCart -> Chuyển sang màn hình xác nhận
    private void OrderCart() {
        //Load lại rcv
        try {
            loadRecyclerViewCart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Kiểm tra nếu chưa có hàng trong cart
        if(cartList.size() == 0){
            Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(getActivity(), ConfirmOrderActivity.class));
    }


    //VII. ClearCart
    private void ClearCart() {
        //Load lại rcv
        try {
            loadRecyclerViewCart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Kiểm tra nếu chưa có hàng trong cart
        if(cartList.size() == 0){
            Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            truyVanSanPham.UpdateStatusAllCart(account.getId_account(), 2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Load lại rcv và tổng tiền
        try {
            loadRecyclerViewCart();
            setTotalMoneyOfCart();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}