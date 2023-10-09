package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.login.LoginActivity;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;
/*
Fragment cha: Sẽ dùng menu để điều khiển các fragment con
Có thể đặt menu sẵn trong fragment cha (Chú ý phần layout dùng để thay các fragment, dùng replace chứ không dùng add để không bị chồng lên nhau
Hiện tại ở đây phần menu đang làm riêng 1 fragment (set sẵn) nên nếu back về tiếp thì sẽ là layout trống
 */
public class AccountFragment extends Fragment {
    private View view;
    private ImageView imgAvatar, img_back_account;
    private TextView txt_email;
    private Account account = DataLocalManager.getAccount();
    private TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();
    public static AccountFragment accountFragment; //Đặt AccountFragment trong chính nó để gọi chính nó (get Instances) từ bên ngoài

    public static AccountFragment Instances(){
        return accountFragment;
    }
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);

        //1. Ánh xạ
        AnhXa();
        accountFragment = this; //Khai báo AccountFragment trong hàm Instances bằng chính fragment đã tạo này

        //2. Bấm vào ảnh để mở Activity chọn ảnh từ máy làm avatar
        imgAvatar.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UploadAvatarActivity.class));
        });

        //3. Sự kiện back
        img_back_account.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });




        return view;
    }



    //I* Load dữ liệu account, ảnh Avatar (mỗi khi truy cập fragment này)
    @Override
    public void onResume() {
        super.onResume();
        txt_email.setText(account.getEmail());

        try {
            byte [] imgAvatarSQL = truyVanAccount.getAvatar(account.getId_account(), 1);

            if(imgAvatarSQL != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imgAvatarSQL, 0, imgAvatarSQL.length);
                imgAvatar.setImageBitmap(bitmap);
            } else {
                imgAvatar.setImageResource(R.drawable.image_avatar_default);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Load fragment Menu mỗi khi vào
        setFragmentChildren2(new MenuAccountFragment());

        //
        setBack(1);
    }

    //1. Ánh xạ
    private void AnhXa() {
        img_back_account = view.findViewById(R.id.imageView_back_account);
        imgAvatar = view.findViewById(R.id.imageView_account_avatar);
        txt_email = view.findViewById(R.id.textView_account_email);

    }


    //2. Hàm set các fragment con có transition
    public void setFragmentChildren (Fragment fragment) {

        //Tạo quản lý fragment ở trong fragment này: FragmentManager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //Tạo FragmentTransaction: thực hiện thay một layout (nằm trong xml của fragment này) bằng các fragment khác
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Hiệu ứng ra vào và khi back về
        fragmentTransaction.setCustomAnimations(R.anim.anim_right_to_left_in, R.anim.anim_right_to_left_out, R.anim.anim_left_to_right_in, R.anim.anim_left_to_right_out);

        //replace: Đặt lại fragment (sẽ chỉ có 1 fragment trong layout), hiệu ứng sẽ chuyển | add: Thêm fragment vào layout (chồng lên fragment hiện tại), hiệu ứng từ phải đi về trái
        fragmentTransaction.replace(R.id.layout_account_view, fragment, "tagMenuAccountFragment");

        //Tên khi back về
        fragmentTransaction.addToBackStack("StackMenuAccountFragment");

        fragmentTransaction.commit();

    }

    //2. Hàm set các fragment con không có transition
    public void setFragmentChildren2 (Fragment fragment) {

        //Tạo quản lý fragment ở trong fragment này: FragmentManager
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //Tạo FragmentTransaction: thực hiện thay một layout (nằm trong xml của fragment này) bằng các fragment khác
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.layout_account_view, fragment, "tagMenuAccountFragment"); //Tag để tìm tên nếu cần

        //Tên khi back về
        fragmentTransaction.addToBackStack("StackMenuAccountFragment");

        fragmentTransaction.commit();

    }

    //3. Ẩn hiện phím back: 1 hiện, 2 ẩn
    public void setBack(int number){
        if(number == 1) {
            img_back_account.setVisibility(View.VISIBLE);
        }
        if(number == 2){
            img_back_account.setVisibility(View.GONE);
        }
    }


}