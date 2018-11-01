package com.example.weioule.mvpdemo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.weioule.mvpdemo.R;
import com.example.weioule.mvpdemo.base.BaseActivity;
import com.example.weioule.mvpdemo.model.WelcomeModel;
import com.example.weioule.mvpdemo.presenter.WelcomePresenter;
import com.example.weioule.mvpdemo.view.WelcomeMvpView;
import com.example.weioule.mvpdemo.widget.ProgressView;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class WelcomeActivity extends BaseActivity<WelcomeMvpView, WelcomeModel, WelcomePresenter> implements WelcomeMvpView, View.OnClickListener {

    private ImageView mBannerImg;
    private int AD_DISPLAY_TIME = 1750;
    private static Handler handler = new Handler();
    private RelativeLayout mCountdownBtn;
    private ProgressView mProgressView;
    private Runnable mRunnable;


    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initView() {
        mBannerImg = findViewById(R.id.banner_img);
        mCountdownBtn = findViewById(R.id.rl_enter);
        mProgressView = findViewById(R.id.progress);
        mBannerImg.setOnClickListener(this);
        mCountdownBtn.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //模拟网络请求
        mBannerImg.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getAdInfo();
            }
        }, 1000);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                forwardAndFinish(HomeActivity.class);
            }
        };
        handler.postDelayed(mRunnable, AD_DISPLAY_TIME);
    }

    @Override
    public void showAdFormCache(Bitmap bitmap, int duration) {
        if (isFinishing() || isDestroyed()) return;
        handler.removeCallbacks(mRunnable);
        mBannerImg.setImageBitmap(bitmap);
        mCountdownBtn.setVisibility(View.VISIBLE);
        mProgressView.startDownTime(duration, new ProgressView.OnFinishListener() {
            @Override
            public void onFinish() {
                forwardAndFinish(HomeActivity.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        mProgressView.stopDownTime();
        switch (v.getId()) {
            case R.id.banner_img:
                forward(HomeActivity.class);
                mPresenter.jumpToAd();
                finish();
                break;
            case R.id.rl_enter:
                forwardAndFinish(HomeActivity.class);
                break;
        }
    }

    @Override
    public Context getContexts() {
        return this;
    }

    @Override
    protected WelcomePresenter createPresenter() {
        return new WelcomePresenter();
    }
}
