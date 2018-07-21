package com.zucc.ccm31501396.mdays;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class UseAcitvity extends AppCompatActivity {
    private static Context sContest = null;
    private ViewPager mViewPager;
    private BottomNavigationView mBottonmNavigationView;
    private TextView back_button;
    private Toolbar mToolbar;

    public static Context getContext() {
        return sContest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_acitvity);

        mViewPager = (ViewPager) findViewById(R.id.fragment_view);

        final ArrayList<Fragment> mfragments = new ArrayList<>(3);
        mfragments.add(new Fragment1());
        mfragments.add(new Fragment2());

        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return mfragments.get(position);
            }

            @Override
            public int getCount() {
                return mfragments.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        mBottonmNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        mBottonmNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_dashboard:
                        mViewPager.setCurrentItem(1);
                        break;
//                    case R.id.navigation_notifications:
//                        mViewPager.setCurrentItem(2);
//                        break;
                }
                return false;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBottonmNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}
