package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanSanPhamMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.AccountFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product.ProductAdapter;
import com.example.prm391x_searchfood_vietcvfx12045.model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsFragment extends Fragment {
    private ImageView imgProduct, imgReduce, imgAdd, imgFavorite;
    private TextView txtNameProduct, txtPrice, txtUnit, txtDes;
    private EditText edtNumberProduct;
    private Button btnAddtoCart;
    private final TruyVanSanPhamMSSQL truyVanSanPham = new TruyVanSanPhamMSSQL();
    private Product product;
    private int id_product;
    private View view;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_details, container, false);

        //1.1 Lấy dữ liệu gửi đến để sử dụng
        Bundle bundle = getArguments();

        if(bundle != null){
            id_product = bundle.getInt("id_product", 1);
        }

        //Lấy product theo id_product được gửi đến
        try {
            product = truyVanSanPham.getProduct(id_product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //1.2 Ánh xạ
        AnhXa();

        //2. Tăng views cho product được xem
        UpdateViewProduct();

        //3. Lấy dữ liệu Product và gán giá trị hiển thị
        ApplieProduct();

        //4.1 Sự kiện click vào icon plus, reduce,
        imgAdd.setOnClickListener(v -> {
            ClickButtonPlus();
        });

        imgReduce.setOnClickListener(v -> {
            ClickButtonReduce();
        });

        //4.2 Sự kện click edt -> Điều chỉnh số lượng và không để bị trống
        edtNumberProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //a. Kiểm tra nếu editText not empty và set giới hạn số lượng
                if(!TextUtils.isEmpty(edtNumberProduct.getText())) {

                    //Check lại số lượng editText (Vì getText là về dạng text nên cần parse sang int)
                    int amount_current = Integer.parseInt(edtNumberProduct.getText().toString().trim());
                    if(amount_current > 1000){
                        edtNumberProduct.setText(1000 + "");
                    }

                    //Set lại editText = 1 nếu empty
                } else {
                    edtNumberProduct.setText(1 + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //5. Click Add to Cart: Thêm dữ liệu vào giỏ hàng
        btnAddtoCart.setOnClickListener(v -> {
            AddToCart();
        });

        //7. Sự kiện cơ bản back về màn hình đã mở lên activity này: Dùng hàm của Activity
        AccountFragment.Instances().setBack(1);

        //8. Click favorite
        imgFavorite.setOnClickListener(v -> {
            ClickFavorite();
        });




        return view;
    }



    //1. Ánh xạ
    private void AnhXa() {
        imgProduct = view.findViewById(R.id.imageView_details_product_fm);
        imgReduce = view.findViewById(R.id.reduce_cart_product_fm);
        imgAdd = view.findViewById(R.id.plus_cart_product_fm);
        imgFavorite = view.findViewById(R.id.img_favorite_product_fm);

        txtNameProduct = view.findViewById(R.id.name_details_product_fm);
        txtPrice = view.findViewById(R.id.price_details_product_fm);
        txtUnit = view.findViewById(R.id.unit_details_product_fm);
        txtDes = view.findViewById(R.id.des_details_product_fm);

        edtNumberProduct = view.findViewById(R.id.editText_number_product_fm);
        btnAddtoCart = view.findViewById(R.id.button_addToCart_fm);

    }

    //2. UpdateViewProduct
    private void UpdateViewProduct() {
        try {
            truyVanSanPham.updateProductViews(id_product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //3. Gán dữ liệu product cho Activity
    private void ApplieProduct() {
        boolean checkFavorite;
        int statusFavorite;

        try {
            checkFavorite = truyVanSanPham.checkExistFavorite(DataLocalManager.getAccount().getId_account(), product.getId_product());
            statusFavorite = truyVanSanPham.getStatusFavorite(DataLocalManager.getAccount().getId_account(), product.getId_product());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Bitmap bitmap = BitmapFactory.decodeByteArray(product.getProduct_image(), 0, product.getProduct_image().length);

        imgProduct.setImageBitmap(bitmap);
        txtNameProduct.setText(product.getProduct_name());
        txtPrice.setText("$" + product.getPrice());
        txtUnit.setText("/" + product.getUnit());
        txtDes.setText(product.getDescription());

        //Gán dữ liệu (ảnh) theo hiện trạng đang có cho nút favorite
        if(checkFavorite && statusFavorite == 1){
            imgFavorite.setImageResource(R.drawable.ic_favorite);
        } else {
            imgFavorite.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    //4. ClickButtonPlus
    private void ClickButtonPlus() {
        //get edit text xong rồi tăng số lên
        String numberStr = edtNumberProduct.getText().toString().trim();
        int number = Integer.parseInt(numberStr);
        number++;

        if(number <= 0) {
            number = 1;
        } else if(number > 1000){
            Toast.makeText(getActivity(), "Can't not > 1000", Toast.LENGTH_SHORT).show();
            return;
        }
        edtNumberProduct.setText(number + "");
    }

    private void ClickButtonReduce() {
        //get edit text xong rồi giảm số
        String numberStr = edtNumberProduct.getText().toString().trim();
        int number = Integer.parseInt(numberStr);
        number--;

        if(number <= 0) number = 1;

        edtNumberProduct.setText(number + "");

    }

    //5. Click Add to Cart: Thêm dữ liệu vào giỏ hàng
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void AddToCart() {
        int id_account = DataLocalManager.getAccount().getId_account();
        String amountStr = edtNumberProduct.getText().toString().trim();
        int amount = Integer.parseInt(amountStr);
        double sum = amount*product.getPrice();

        try {
            truyVanSanPham.InsertProductToCart(id_account, id_product, amount, sum);
            Toast.makeText(getActivity(), "Đã thêm: " + product.getProduct_name(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //7. Sự kiện cơ bản back về màn hình đã mở lên activity này: Dùng hàm của Activity
    private void Back() {
        getActivity().getSupportFragmentManager().popBackStack();
//      overridePendingTransition(R.anim.anim_left_to_right_in, R.anim.anim_left_to_right_out);
    }

    //8. Click nút favorite
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void ClickFavorite() {

        //Đặt lại tên dữ liệu
        int id_account = DataLocalManager.getAccount().getId_account();
        int id_product_favorite = product.getId_product();

        try {
            //a. Kiểm tra xem có tồn tại trong CSDL (Đã từng click) | Phải kiểm tra riêng lại trong từng click
            boolean checkFavorite_current = truyVanSanPham.checkExistFavorite(id_account, id_product_favorite);

            //b. Nếu chưa tồn tại thì thêm vào CSDL (trạng thái yêu thích là 1) | cập nhật ảnh icon
            if(!checkFavorite_current){

                truyVanSanPham.insertFavorite(id_account, id_product_favorite);
                imgFavorite.setImageResource(R.drawable.ic_favorite);

                //c. Nếu đã tồn tại trong CSDL thì cập nhật trạng thái Favorite
            } else {

                //Lấy status của Favorite | Cập nhật giữa hai trạng thái 1 (yêu thích) và 2 (xoá, là đã từng thích, từng tương tác, nhưng được lưu lại chứ không xoá khỏi CSDL)
                int status = truyVanSanPham.getStatusFavorite(id_account, id_product_favorite);

                //Cập nhật status và ảnh icon tương ứng (đang là 1 thì chuyển sang 2, đang là 2 thì chuyển về 1)
                if(status == 1){
                    truyVanSanPham.updateStatusFavorite(id_account, id_product_favorite, 2);
                    imgFavorite.setImageResource(R.drawable.ic_favorite_border);
                } else if (status == 2) {
                    truyVanSanPham.updateStatusFavorite(id_account, id_product_favorite, 1);
                    imgFavorite.setImageResource(R.drawable.ic_favorite);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}