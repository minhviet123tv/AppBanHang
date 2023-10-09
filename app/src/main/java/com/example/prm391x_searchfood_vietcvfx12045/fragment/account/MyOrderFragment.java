package com.example.prm391x_searchfood_vietcvfx12045.fragment.account;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm391x_searchfood_vietcvfx12045.R;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.fragment.account.myorder.MyOrderTabViewPagerAdapter;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.google.android.material.tabs.TabLayout;

public class MyOrderFragment extends Fragment {
    private TabLayout tabLayoutOrder;
    private ViewPager viewPagerOrder;
    private MyOrderTabViewPagerAdapter orderTabViewPagerAdapter;
    private Account account;

    private View view;

    public MyOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_order, container, false);

        //1. Ánh xạ
        AnhXa();

        //2. Khai báo adapter Tab
        orderTabViewPagerAdapter = new MyOrderTabViewPagerAdapter(getChildFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        viewPagerOrder.setAdapter(orderTabViewPagerAdapter);
        tabLayoutOrder.setupWithViewPager(viewPagerOrder);

        //3. Set sẵn tab đầu tiên khi mới vào
        viewPagerOrder.setCurrentItem(0);

        return view;
    }

    //1. Ánh xạ
    private void AnhXa() {
        tabLayoutOrder = view.findViewById(R.id.tab_my_order);
        viewPagerOrder = view.findViewById(R.id.viewPager_tab_my_order);
    }

    @Override
    public void onResume() {
        super.onResume();

        //Hiện nút back ở trang này
        AccountFragment.Instances().setBack(1);

        //Load account
        account = DataLocalManager.getAccount();

    }
}