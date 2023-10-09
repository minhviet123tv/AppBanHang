package com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prm391x_searchfood_vietcvfx12045.model.Categories;

import java.util.List;
/*
Adapter xử lý Fragment cho ViewPager của Tab Product
 */
public class TabProductViewPagerAdapter extends FragmentStatePagerAdapter {
    //Tạo list Categories
    private List<Categories> listCategories;

    //I. Hàm khởi tạo
    public TabProductViewPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Categories> listCategories) {
        super(fm, behavior);
        this.listCategories = listCategories;
    }

    //II. Hàm trả về fragment tương ứng vị trí chọn Tab (bắt đầu từ 0 -> 8) | Truyền id_categories vào tạo mới fragment (bắt đầu từ 1 theo CSDL)
    // -> Mỗi khi click vào vị trí Tab lại có fragment được tạo với id_categories
    @NonNull
    @Override
    public Fragment getItem(int position) {
//        return new Tab1ProductFragment(listCategories.get(position).getId_categories());
        switch (position){
            case 0:
                return new Tab1ProductFragment();
            case 1:
                return new Tab2ProductFragment();
            case 2:
                return new Tab3ProductFragment();
            case 3:
                return new Tab4ProductFragment();
            case 4:
                return new Tab5ProductFragment();
            case 5:
                return new Tab6ProductFragment();
            case 6:
                return new Tab7ProductFragment();
            case 7:
                return new Tab8ProductFragment();
            case 8:
                return new Tab9ProductFragment();
            default:
                //Có thể dùng default để dùng cho tất cả các tab nếu truyền và cập nhật được dữ liệu cần thiết vào fragment
                return new Tab1ProductFragment();
        }
    }

    //III. Số lượng tab fragment (Theo listCategories truyền vào, hiện tại là 9) theo list lấy được từ CSDL
    // -> Như vậy các position của các hàm kia sẽ là từ 0 -> 8
    @Override
    public int getCount() {
        return listCategories.size();
    }

    //IV. Tên tab tương ứng vị trí Tab (bắt đầu từ 0 -> 8): Sử dụng tên Categories
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return  listCategories.get(position).getCategories_name();
    }
}
