package com.example.weioule.mvpdemo.base;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weioule.mvpdemo.R;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
@SuppressWarnings({"unchecked", "deprecation"})
public abstract class BaseTitleActivity<V extends IView, P extends BasePresenter> extends BaseActivity<V, P> {

    protected TextView mTitleText;
    protected TextView mLeftText;
    protected TextView mRightText;
    protected ImageView mLeftImage;
    protected ImageView mRightImage;
    protected RelativeLayout titleBar;

    @Override
    protected final int getLayoutId() {
        return R.layout.activity_title_base;
    }

    @Override
    protected final void initView() {
        titleBar = findViewById(R.id.title_layout);
        mTitleText = titleBar.findViewById(R.id.title_tv_title);
        mLeftImage = titleBar.findViewById(R.id.title_iv_left);
        mLeftText = titleBar.findViewById(R.id.title_tv_left);
        mRightImage = titleBar.findViewById(R.id.title_iv_right);
        mRightText = titleBar.findViewById(R.id.title_tv_right);
        mLeftImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLeftImageClick();
            }
        });

        int body = getLayoutIds();
        if (body != 0) {
            LayoutInflater.from(this).inflate(body, (ViewGroup) findViewById(R.id.container), true);
        }

        initViews();
    }

    protected abstract int getLayoutIds();

    protected abstract void initViews();

    public void showErrorViews() {
        showErrorView(mTitleText, null);
    }

    public void showErrorViews(String errorMsg) {
        showErrorView(mTitleText, errorMsg);
    }

    protected void onLeftImageClick() {
        finish();
    }

    protected void hideLeftImage() {
        mLeftImage.setVisibility(View.GONE);
    }

    protected void addLeftImage(@DrawableRes int resid) {
        addLeftImage(getResources().getDrawable(resid));
    }

    protected void addLeftImage(Drawable drawable) {
        mLeftImage.setImageDrawable(drawable);
    }

    protected void addRightImage(@DrawableRes int resid) {
        addRightImage(getResources().getDrawable(resid));
    }

    protected void addRightImage(Drawable drawable) {
        mRightImage.setImageDrawable(drawable);
        mRightImage.setVisibility(View.VISIBLE);
    }
}
