package com.example.weioule.mvpdemo.activity;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weioule.mvpdemo.Fragment.HomeFragment;
import com.example.weioule.mvpdemo.R;
import com.example.weioule.mvpdemo.adapter.HomeActivityAdapter;
import com.example.weioule.mvpdemo.base.BaseActivity;
import com.example.weioule.mvpdemo.presenter.HomeActivityPresenter;
import com.example.weioule.mvpdemo.view.HomeActivityMvpView;
import com.example.weioule.mvpdemo.widget.CustomViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeActivity extends BaseActivity<HomeActivityMvpView, HomeActivityPresenter> implements HomeActivityMvpView {

    private String[] texts = {"主页", "我的"};
    private boolean mBackKeyPressed = false;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        mViewPager = findViewById(R.id.viewpager);
        mTabLayout = findViewById(R.id.navigation_tab);
        mViewPager.setOffscreenPageLimit(2);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment(texts[0]));
        fragments.add(new HomeFragment(texts[1]));
        mViewPager.setAdapter(new HomeActivityAdapter(getSupportFragmentManager(), fragments));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setCustomView(getTabView(0));
        mTabLayout.getTabAt(1).setCustomView(getTabView(1));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabStatus(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabStatus(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void initData() {
        mPresenter.checkVersion();
    }

    @Override
    public void checkVersion(boolean msg) {
        mPresenter.showUpdateDialog(this);
    }

    @Override
    public void upgrade() {
        forward(WebviewActivity.class);
    }

    private void changeTabStatus(TabLayout.Tab tab, boolean selected) {
        View view = tab.getCustomView();
        TextView tv = view.findViewById(R.id.tab_tv);
        ImageView iv = view.findViewById(R.id.tab_iv);
        tv.setSelected(selected);
        iv.setSelected(selected);
    }

    public View getTabView(final int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView tv = view.findViewById(R.id.tab_tv);
        ImageView iv = view.findViewById(R.id.tab_iv);
        tv.setText(texts[position]);
        if (position == 0) {
            tv.setSelected(true);
            iv.setSelected(true);
            iv.setImageResource(R.drawable.bottom_nav_icon_main);
        } else {
            iv.setImageResource(R.drawable.bottom_nav_icon_user);
            tv.setSelected(false);
            iv.setSelected(false);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(position);
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            toast(R.string.exit_app);
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {
                // 延时两秒，如果超出则取消取消上一次的记录
                @Override
                public void run() {
                    mBackKeyPressed = false;

                }
            }, 2000);
        } else {// 退出程序
            this.finish();
            System.exit(0);
        }
    }

    @Override
    public Context getContexts() {
        return this;
    }

    @Override
    protected HomeActivityPresenter createPresenter() {
        return new HomeActivityPresenter();
    }
}
