package com.zucc.ccm31501396.mdays.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mac on 2018/7/12.
 */

public class FragmentPageAdapter extends FragmentPagerAdapter{
    public List<Fragment> mFragmentList;
    public FragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        mFragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList == null?null:mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null?0:mFragmentList.size();
    }
}
