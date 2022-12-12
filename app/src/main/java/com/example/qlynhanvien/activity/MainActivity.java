package com.example.qlynhanvien.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.qlynhanvien.R;
import com.example.qlynhanvien.adapter.FragmentAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mNavigationView;
    private ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationView = findViewById(R.id.bottomNav);
        mViewPager = findViewById(R.id.viewPager);
        setUpViewPager();
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_department:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_employee:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpViewPager(){
        FragmentAdapter viewPagerAdapter = new FragmentAdapter(this);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        mNavigationView.getMenu().findItem(R.id.nav_department).setChecked(true);
                        break;
                    case 2:
                        mNavigationView.getMenu().findItem(R.id.nav_employee).setChecked(true);
                        break;
                }
            }
        });
    }
}