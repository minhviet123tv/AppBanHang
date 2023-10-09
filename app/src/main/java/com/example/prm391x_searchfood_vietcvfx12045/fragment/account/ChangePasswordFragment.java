package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.example.prm391x_searchfood_vietcvfx12045.model.User;

import java.sql.SQLException;


public class ChangePasswordFragment extends Fragment {

    private EditText edt_old_password, edt_new_password, edt_confirm_password;
    private Button btn_save, btn_cancel;
    private Account account = DataLocalManager.getAccount();
    private TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();
    private View view;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);

        //1. Ánh xạ
        AnhXa();

        //2. Sự kiện click cancel -> Trở về fragment trước
        btn_cancel.setOnClickListener(v -> {
            Cancel();
        });

        //3. Sự kiện click save -> Kiểm tra nhập dữ liệu -> Kiểm tra old password -> Khớp new password -> Cấu trúc password -> lưu vào csdl
        btn_save.setOnClickListener(v -> {
            try {
                Save();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });



        return view;
    }

    //3. Save -> Kiểm tra nhập dữ liệu -> Kiểm tra old password -> Khớp new password -> Cấu trúc password -> lưu vào csdl
    private void Save() throws SQLException {

        //a. lấy dữ liệu nhập vào editText
        String old_password = edt_old_password.getText().toString().trim();
        String new_password = edt_new_password.getText().toString();
        String cofirm_new_password = edt_confirm_password.getText().toString();

        //b. Kiểm tra dữ liệu nhập vào, nếu có một trong các edt trống thì thông báo
        if(TextUtils.isEmpty(old_password) || TextUtils.isEmpty(new_password) || TextUtils.isEmpty(cofirm_new_password)){
            Toast.makeText(requireActivity(), R.string.toast_enter_text, Toast.LENGTH_SHORT).show();

        //c. Nếu đã điền đầy đủ thì xử lý tiếp check old password
        } else {

            Account account_confirm = truyVanAccount.getAccount(account.getEmail(), account.getPassword());

            //Nếu không khớp mật khẩu old password với của tài khoản hiện tại
            if(!account_confirm.getPassword().equals(old_password)){
                Toast.makeText(requireActivity(), R.string.toast_wrong_password, Toast.LENGTH_SHORT).show();

            //Nếu khớp mật khẩu với tài khoản hiện tại -> kiểm tra 2 mật khẩu mới
            } else {

                //Nếu 2 mật khẩu không khớp
                if(!new_password.equals(cofirm_new_password)){
                    Toast.makeText(requireActivity(), R.string.toast_wrong_new_password, Toast.LENGTH_SHORT).show();

                //Nếu 2 mật khẩu khớp -> kiểm tra tính hợp lệ của mật khẩu (Dùng chức năng kiểm trang tương tự Model User)
                } else {

                    if (new_password.length() < 8) {
                        Toast.makeText(requireActivity(), R.string.password_8_characters, Toast.LENGTH_SHORT).show();

                    } else if (new_password.matches("\\w*\\s+\\w*")) {
                        Toast.makeText(requireActivity(), R.string.toast_password_not_space, Toast.LENGTH_SHORT).show();

                    } else {

                        //Update mật khẩu vào CSDL và local
                        truyVanAccount.UpdateAccountPassword(account.getId_account(), new_password);

                        Account accountUpdate = truyVanAccount.getAccount(account.getEmail(), new_password);
                        DataLocalManager.setAccount(accountUpdate);

                        //Thông báo
                        Toast.makeText(requireActivity(), R.string.toast_change_password_success, Toast.LENGTH_SHORT).show();
                        //Trở về fragment trước (Có thể back 2 lần về menu nếu cần thiết)
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }

                }
            }
        }
    }

    //2. Cancel -> Trở về fragment trước (Có thể back 2 lần về menu nếu cần thiết)
    private void Cancel() {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    //1. Ánh xạ
    private void AnhXa() {
        edt_old_password = view.findViewById(R.id.editText_old_password);
        edt_new_password = view.findViewById(R.id.editText_new_password);
        edt_confirm_password = view.findViewById(R.id.editText_confirm_new_password);

        btn_save = view.findViewById(R.id.button_change_password);
        btn_cancel = view.findViewById(R.id.button_cancel_change_password);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Hiện nút back ở trang này
        AccountFragment.Instances().setBack(1);
    }
}