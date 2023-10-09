package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;

import java.sql.SQLException;
import java.text.Normalizer;

public class ProfileFragment extends Fragment {
    private View view;
    private ImageView img_profile_edit_phone, img_profile_check_editText_phone, img_address_edit_text, img_profile_check_editText_address, img_account_change_password, img_change_password_arrow_right;
    private LinearLayout layout_textView_profile, layout_edt_profile, layout_textView_address, layout_edt_address;
    private EditText edt_profile_phone, edt_profile_address;
    private TextView txt_profile_phone, txt_profile_address, txt_account_change_password;
    private Account account = DataLocalManager.getAccount();
    private final TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        //1. Ánh xạ
        AnhXa();

        //2. Khi click vào hình edit Text của phone (khi đang hiện textView) -> Hiện editText
        img_profile_edit_phone.setOnClickListener(v -> {
            EditPhone();
        });

        //3. Check vào phím check text của phone (khi edit xong) -> Gán hiện textView -> Update CSDL và local
        img_profile_check_editText_phone.setOnClickListener(v -> {
            ConfirmPhone();
        });

        //4. Click vào hình edit Text của Address (khi đang hiện textView) -> Hiện editText
        img_address_edit_text.setOnClickListener(v -> {
            EditAddress();
        });

        //5. Check vào phím check của Address (khi edit xong) -> Gán hiện textView -> Update CSDL và local
        img_profile_check_editText_address.setOnClickListener(v -> {
            ConfirmAddress();
        });

        //6. Mở activity thay đổi mật khẩu
        txt_account_change_password.setOnClickListener(v -> {
            OpenChangePassword();
        });
        img_account_change_password.setOnClickListener(v -> {
            OpenChangePassword();
        });
        img_change_password_arrow_right.setOnClickListener(v -> {
            OpenChangePassword();
        });




        return view;
    }

    //6. Mở activity thay đổi mật khẩu
    private void OpenChangePassword() {
        AccountFragment.Instances().setFragmentChildren(new ChangePasswordFragment());
    }


    //5. Check vào phím check của Address (khi edit xong) -> Gán hiện textView -> Update CSDL và local
    private void ConfirmAddress() {
        //Ẩn và hiện đối tượng tương ứng
        layout_textView_address.setVisibility(View.VISIBLE);
        layout_edt_address.setVisibility(View.GONE);

        //Ẩn bàn phím
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        //Gán dữ liệu cho textView hiển thị | Lấy kiểu UTF-8
        String address2 = Normalizer.normalize(edt_profile_address.getText().toString().trim(), Normalizer.Form.NFC);
//        String address = edt_profile_address.getText().toString().trim();
        txt_profile_address.setText(address2);



        Account account_from_sql;
        try {
            // Update CSDL và cập nhật lại thông tin account
            truyVanAccount.UpdateAccountAddress(account.getId_account(), address2);

            //lấy lại thông tin account sau khi đã cập nhật
            account_from_sql = truyVanAccount.getAccount(DataLocalManager.getAccount().getEmail(), DataLocalManager.getAccount().getPassword());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Cập nhật lại cho local
        DataLocalManager.setAccount(account_from_sql);
    }


    //4. Click vào hình edit Text của Address (khi đang hiện textView) -> Hiện editText
    private void EditAddress() {
        //Ẩn và hiện đối tượng tương ứng
        layout_textView_address.setVisibility(View.GONE);
        layout_edt_address.setVisibility(View.VISIBLE);

        //Hiện con trỏ vào edt
        edt_profile_address.requestFocus();
        edt_profile_address.setSelection(edt_profile_address.length());

        //Hiện bàn phím khi bấm vào nút check
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        //Sự kiện khi out focus
        edt_profile_address.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus == false){
                layout_textView_address.setVisibility(View.VISIBLE);
                layout_edt_address.setVisibility(View.GONE);
            }
        });
    }

    //3. ConfirmPhone
    private void ConfirmPhone() {
        //Ẩn và hiện đối tượng tương ứng
        layout_textView_profile.setVisibility(View.VISIBLE);
        layout_edt_profile.setVisibility(View.GONE);

        //Ẩn bàn phím
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        //Gán dữ liệu cho textView hiển thị
        String phone = edt_profile_phone.getText().toString().trim();
        txt_profile_phone.setText(phone);

        try {
            // Update CSDL và cập nhật lại thông tin account
            truyVanAccount.UpdateAccountPhone(account.getId_account() ,phone);

            //lấy lại thông tin account sau khi đã cập nhật
            account = truyVanAccount.getAccount(DataLocalManager.getAccount().getEmail(), DataLocalManager.getAccount().getPassword());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Cập nhật lại cho local
        DataLocalManager.setAccount(account);
    }


    //2. EditPhone
    private void EditPhone() {

        //Ẩn và hiện đối tượng tương ứng
        layout_textView_profile.setVisibility(View.GONE);
        layout_edt_profile.setVisibility(View.VISIBLE);

        //Hiện con trỏ vào editText
        edt_profile_phone.requestFocus(); //Chỉ con trỏ vào edt
        edt_profile_phone.setSelection(edt_profile_phone.getText().length()); //Vị trí là cuối edt

        //Hiện bàn phím (So với dùng ở Activity thì phải thêm getActivity, hoặc requireActivity() | nghe nói để tránh lỗi)
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        //Sự kiện khi có thay đổi focus -> Hiện TextView và ẩn EditText
        edt_profile_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                //Out focus
                if(hasFocus == false){
                    layout_textView_profile.setVisibility(View.VISIBLE);
                    layout_edt_profile.setVisibility(View.GONE);
//                    v.clearFocus();
                }
            }
        });

    }

    //II* Load các thông tin mỗi khi mở fragment này
    @Override
    public void onResume() {
        super.onResume();

        txt_profile_phone.setText(account.getPhone());
        edt_profile_phone.setText(account.getPhone());
        txt_profile_address.setText(account.getAddress());
        edt_profile_address.setText(account.getAddress());

        //Hiện nút back ở trang này
        AccountFragment.Instances().setBack(1);
    }

    //1. Ánh xạ
    private void AnhXa() {

        img_profile_edit_phone = view.findViewById(R.id.img_profile_edit_phone);
        img_profile_check_editText_phone = view.findViewById(R.id.img_profile_check_editText_phone);
        img_profile_check_editText_address = view.findViewById(R.id.img_profile_check_editText_address);
        img_account_change_password = view.findViewById(R.id.imageView_account_change_password);
        img_change_password_arrow_right = view.findViewById(R.id.imageView_change_password_arrow_right);

        edt_profile_phone = view.findViewById(R.id.edt_profile_phone);
        edt_profile_address = view.findViewById(R.id.edt_profile_address);

        img_address_edit_text = view.findViewById(R.id.img_address_edit_text);

        layout_textView_profile = view.findViewById(R.id.layout_profile_phone);
        layout_edt_profile = view.findViewById(R.id.edt_profile);
        layout_textView_address = view.findViewById(R.id.layout_textView_address);
        layout_edt_address = view.findViewById(R.id.layout_edt_address);

        txt_profile_phone = view.findViewById(R.id.textView_profile_phone);
        txt_profile_address = view.findViewById(R.id.textView_profile_address);
        txt_account_change_password = view.findViewById(R.id.textView_account_change_password);

    }


}