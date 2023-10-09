package com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyOrderTabViewPagerAdapter extends FragmentStatePagerAdapter {

    //I. Hàm khởi tạo
    public MyOrderTabViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    //II. Hàm trả về fragment tương ứng vị trí chọn Tab
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new OrderTab1_Fragment(1);
            case 1:
                return new OrderTab1_Fragment(2);
            case 2:
                return new OrderTab1_Fragment(3);
            case 3:
                return new OrderTab1_Fragment(4);
            case 4:
                return new OrderTab1_Fragment(5);
            case 5:
                return new OrderTab1_Fragment(6);
            default:
                return new OrderTab1_Fragment(1);
        }

    }

    //III. Số lượng tab fragment
    @Override
    public int getCount() {
        return 6;
    }

    //IV. Tên tab tương ứng fragment
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Đã đặt mua";
            case 1:
                return "Chờ ship hàng";
            case 2:
                return "Đang ship hàng";
            case 3:
                return "Đã nhận hàng";
            case 4:
                return "Đã huỷ mua";
            case 5:
                return "Đã trả lại";
            default:
                return "Khác";
        }
    }
}
