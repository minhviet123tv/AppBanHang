package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.MainActivity;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.login.LoginActivity;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

public class MenuAccountFragment extends Fragment {
    private View view;
    private ImageView img_my_order, img_arrow_my_order, img_profile, img_profile_arrow_right, img_account_logout;
    private TextView txt_my_order, txt_profile, txt_account_logout;
    private Account account = DataLocalManager.getAccount();
    private TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();
    private MainActivity mainActivity;

    public MenuAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_menu_account, container, false);

        //1. Ánh xạ
        AnhXa();

        //2. Sự kiện chuyển sang màn hình My Order
        img_my_order.setOnClickListener(v -> {
            MyOrder();
        });
        txt_my_order.setOnClickListener(v -> {
            MyOrder();
        });
        img_arrow_my_order.setOnClickListener(v -> {
            MyOrder();
        });

        //3. Sự kiện chuyển sang màn hình Profile
        img_profile.setOnClickListener(v -> {
            setFragmentChildren(new ProfileFragment());
        });
        txt_profile.setOnClickListener(v -> {
            setFragmentChildren(new ProfileFragment());
        });
        img_profile_arrow_right.setOnClickListener(v -> {
            setFragmentChildren(new ProfileFragment());
        });

        //5. sự kiện logout (xoá lưu account ở local, chuyển về màn hình login)
        img_account_logout.setOnClickListener(v -> {
            Logout();
        });
        txt_account_logout.setOnClickListener(v -> {
            Logout();
        });
        
        
        

        return view;
    }

    

    //4. Hàm logout: Xoá lưu trữ account ở máy (local), chuyển về màn hình login, thông báo
    private void Logout(){
        DataLocalManager.deleteAccount();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        Toast.makeText(getActivity(), "Logged Out!", Toast.LENGTH_SHORT).show();
    }
    
    //3. MyOrder() -> Mở fragment MyOrder | Có thể thay fragment trong layout con hoặc thay cả layout lớn của cả trang fragment
    private void MyOrder() {

        //Thay fragment cha trực tiếp tại fragment con nơi có sự kiện click (Cũng có thể dùng hàm static của AccountFragment)
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Hiệu ứng chuyển cảnh fragment | 2 hiệu ứng đầu gồm: hiệu ứng fragment vào và hiệu ứng fragment ra | 2 hiệu ứng sau sử dụng khi back về fragment này: hiệu ứng vào, hiệu ứng ra
        fragmentTransaction.setCustomAnimations(R.anim.anim_right_to_left_in, R.anim.anim_right_to_left_out, R.anim.anim_left_to_right_in, R.anim.anim_left_to_right_out);

        //replace: Đặt lại fragment (sẽ chỉ có 1 fragment trong layout), hiệu ứng sẽ chuyển | add: Thêm fragment vào layout (chồng lên fragment hiện tại), hiệu ứng từ phải đi về trái
        fragmentTransaction.replace(R.id.layout_account_view, new MyOrderFragment(), "tagMyOrderFragment"); //đặt tên tag | layout_account_page : Toàn bộ layout của AccountFragment
        fragmentTransaction.addToBackStack("StackMyOrderFragment"); //Thêm vào ngăn xếp Stack để PopBackStack
        fragmentTransaction.commit();
    }

    //2. Hàm set các fragment con (nằm trong fragment AccountFragment)
    public void setFragmentChildren(Fragment fragment){
        AccountFragment.Instances().setFragmentChildren(fragment);
    }

    //1. Ánh xạ
    private void AnhXa() {

        img_my_order = view.findViewById(R.id.imageView_account_my_order);
        txt_my_order = view.findViewById(R.id.textView_account_my_order);
        img_arrow_my_order = view.findViewById(R.id.imageView_my_order_arrow_right);

        img_profile = view.findViewById(R.id.imageView_account_my_profile);
        img_profile_arrow_right = view.findViewById(R.id.imageView_my_profile_arrow_right);
        txt_profile = view.findViewById(R.id.textView_account_my_profile);

        img_account_logout = view.findViewById(R.id.imageView_account_logout);
        txt_account_logout = view.findViewById(R.id.textView_account_my_logout);

        mainActivity = (MainActivity) requireActivity();

    }

    @Override
    public void onResume() {
        super.onResume();

        //Ẩn nút back trang khi ở menu chính (còn fragment khác thì hiện)
        AccountFragment.Instances().setBack(2);
    }
}