package com.example.weioule.mvpdemo.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.weioule.mvpdemo.R;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public abstract class BaseActivity<V extends IView, M extends BaseModel, P extends BasePresenter<V, M>> extends AppCompatActivity {

    protected P mPresenter;
    protected TextView mErrorContent;
    protected RelativeLayout mErrorRl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(this).resumeRequests();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Glide.with(this).pauseRequests();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mPresenter = null;
        super.onDestroy();
    }

    protected void showErrorView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_net_error, null, false);
        setContentView(view);
        mErrorRl = findViewById(R.id.rl_net_error);
        mErrorContent = findViewById(R.id.content_error);
        mErrorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(getApplicationContext())) {
                    hideErrorView();
                } else {
                    toast(R.string.no_network);
                }
            }
        });
    }

    protected void hideErrorView() {
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected final void toast(int resid) {
        SingletonToast.getInstance(getApplicationContext()).show(resid);
    }

    protected final void toast(int resid, int duration) {
        SingletonToast.getInstance(getApplicationContext()).show(resid, duration);
    }

    protected final void toast(String strText) {
        SingletonToast.getInstance(getApplicationContext()).show(strText);
    }

    protected final void toast(String strText, int duration) {
        SingletonToast.getInstance(getApplicationContext()).show(strText, duration);
    }

    protected void forwardAndFinish(Class<?> cls) {
        forward(cls);
        finish();
    }

    protected void forward(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    protected int getNavigationBarHeight(Context context) {
        int resourceId;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        } else
            return 0;
    }

    /**
     * 判断网络
     */
    public boolean isNetworkAvailable(@NonNull Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
