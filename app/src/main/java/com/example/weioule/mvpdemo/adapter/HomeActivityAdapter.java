package com.example.weioule.mvpdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeActivityAdapter extends FragmentPagerAdapter {

    private List<Fragment> mDatas;

    public HomeActivityAdapter(FragmentManager fm, List<Fragment> datas) {
        super(fm);
        this.mDatas = datas;
    }

    @Override
    public Fragment getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public int getCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size();
    }

}
