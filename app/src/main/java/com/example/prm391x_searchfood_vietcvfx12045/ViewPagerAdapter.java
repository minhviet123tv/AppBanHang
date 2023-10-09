package com.example.prm391x_searchfood_vietcvfx12045;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.AccountFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.MyOrderFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.cart.CartFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.explore.ExploreFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.favorite.FavoriteFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.tab_product.TabProductFragment;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.home.HomeFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //Khởi tạo
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    //Set fragment tương ứng vị trí
    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ExploreFragment();
            case 2:
                return new CartFragment();
            case 3:
                return new FavoriteFragment();
            case 4:
                return new AccountFragment();
            case 5:
                return new TabProductFragment();
            default:
                return new HomeFragment();
        }
    }

    //Tổng số fragment
    @Override
    public int getCount() {
        return 7;
    }
}
