package com.example.prm391x_searchfood_vietcvfx12045;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.WindowManager;

import com.example.prm391x_searchfood_vietcvfx12045.MSSQL.TruyVanAccountMSSQL;
import com.example.prm391x_searchfood_vietcvfx12045.data_local.DataLocalManager;
import com.example.prm391x_searchfood_vietcvfx12045.model.Account;
import com.example.prm391x_searchfood_vietcvfx12045.wiget.ThietKeViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private ThietKeViewPager viewPager; //Sử dụng ViewPager (không phải ViewPager2) thì setup có vẻ mượt hơn (Có thể ViewPager2 cần xem hướng dẫn chuẩn)
    private BottomNavigationView navigationView;
    private TruyVanAccountMSSQL truyVanAccount = new TruyVanAccountMSSQL();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. Ánh xạ
        viewPager = findViewById(R.id.viewPager_main);
        navigationView = findViewById(R.id.bottomNavigation);

        //fix wakelock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();


        //2. Lấy dữ liệu đăng nhập từ SignIn chuyển sang đây
        Intent intent = getIntent();
        String email = intent.getStringExtra("emailSignIn");
        String password = intent.getStringExtra("passwordSignIn");

        //3. Nếu lần đầu đăng nhập ->  lưu local -> Lần sau (tự) lấy local để login (chứ không phải dùng SignIn nữa)
        if(email != null && password != null) {
            try {
                Account account = truyVanAccount.getAccount(email, password);
                DataLocalManager.setAccount(account); //Lưu local

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        //4.1 Bật tắt Swipe (kéo ngang) fragment cho (các tab) ViewPager. Cài đặt tính năng swipe (sử dụng custom ViewPager tự tạo)
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        viewPager.setEnableSwipe(false);

        //4.2 Sự kiện khi swipe fragment: Chọn menu khớp với vị trí fragment
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //Set menu tương ứng với vị trí của fragment
                setMenuToFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //4.3 Sự kiện khi click vào item menu: Khớp fragment với menu
        navigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.menu_home){
                viewPager.setCurrentItem(0);

            } else if (item.getItemId() == R.id.menu_explore) {
                viewPager.setCurrentItem(1);

            } else if (item.getItemId() == R.id.menu_cart) {
                viewPager.setCurrentItem(2);

            } else if (item.getItemId() == R.id.menu_favorite) {
                viewPager.setCurrentItem(3);

            } else if (item.getItemId() == R.id.menu_account) {
                viewPager.setCurrentItem(4);
            }

            return true;
        });

    }

    //I. Set menu tương ứng với vị trí của fragment
    public void setMenuToFragment(int numberFragment){
        switch (numberFragment){
            case 0:
                navigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                break;
            case 1:
                navigationView.getMenu().findItem(R.id.menu_explore).setChecked(true);
                break;
            case 2:
                navigationView.getMenu().findItem(R.id.menu_cart).setChecked(true);
                break;
            case 3:
                navigationView.getMenu().findItem(R.id.menu_favorite).setChecked(true);
                break;
            case 4:
                navigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
                break;
            case 5:
                navigationView.getMenu().findItem(R.id.menu_account).setChecked(true);
                break;
            default:
                navigationView.getMenu().findItem(R.id.menu_explore).setChecked(true);
                break;
        }
    }

    //II. Set fragment theo vị trí (trong ViewPagerAdapter) | Chú ý: viewPager thì có thể set theo vị trí fragment, nhưng icon menu thì chỉ có 5
    public void setFragment(int numberFragment){
        viewPager.setCurrentItem(numberFragment);

        //Đặt icon menu tương ứng với fragment theo tự điều chỉnh
        if(numberFragment <= 4) {
            setMenuToFragment(numberFragment);

        } else if(numberFragment == 5) {
            setMenuToFragment(1);
        } else if(numberFragment == 6) {
            setMenuToFragment(4);
        } else if(numberFragment == 7) {
            setMenuToFragment(4);
        }
    }

    //III. Sự kiện phím Back cứng (Lưu trữ)
    @Override
    public void onBackPressed() {

        //Kiểm tra số lượng trong ngăn xếp Stack, nếu còn thì back fragment
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        } else {
            //Nếu hết fragment đợi trong ngăn xếp Stack thì Thoát ra màn hình home của điện thoại
            super.onBackPressed();
        }
    }

}