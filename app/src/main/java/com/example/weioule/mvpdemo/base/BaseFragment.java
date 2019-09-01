package com.example.weioule.mvpdemo.base;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weioule.mvpdemo.R;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public abstract class BaseFragment<V extends IView, P extends BasePresenter> extends Fragment {

    private boolean isFirstUserHint = true;
    protected FragmentActivity mActivity;
    private FrameLayout rootFragmentView;
    protected RelativeLayout mErrorRl;
    protected TextView mErrorContent;
    private View normalFragmentView;
    private boolean mFirstOpen;
    private View mErrorView;
    protected P mPresenter;

    public BaseFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        mPresenter = createPresenter();
        if (null != mPresenter)
            mPresenter.attachView((V) this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mFirstOpen = true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootFragmentView = new FrameLayout(mActivity);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootFragmentView.setLayoutParams(params);
        rootFragmentView.removeAllViews();
        normalFragmentView = inflater.inflate(getLayoutId(), container, false);
        rootFragmentView.addView(normalFragmentView);
        initView(rootFragmentView, savedInstanceState);
        return rootFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    /**
     * 这里需要注意：
     * 当HomActivity的Adapter已经添加过改Fragment了，然后网络错误再重新加载数据时的initView()布局初始化中，重新添加该Fragment时，
     * 是不会再重新走它的完整的生命周期方法，而是走onCreate()之后直接走了这里的显示方法，其他生命周期方法将不再执行
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isFirstUserHint) {
            isFirstUserHint = !isFirstUserHint;
            return;
        }
        if (isVisibleToUser) {
            doFragmentResume();
        } else {
            doFragmentPause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            doFragmentPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirstOpen) {
            mFirstOpen = false;
            return;
        }
        if (getUserVisibleHint()) {
            doFragmentResume();
        }
    }

    private void doFragmentPause() {
    }

    private void doFragmentResume() {
    }

    protected void showErrorView(String errorMsg) {
        mErrorView = LayoutInflater.from(mActivity).inflate(R.layout.view_net_error, null, false);
        rootFragmentView.addView(mErrorView);
        mErrorRl = mErrorView.findViewById(R.id.rl_net_error);
        mErrorContent = mErrorView.findViewById(R.id.content_error);
        if (!TextUtils.isEmpty(errorMsg)) {
            mErrorContent.setText(errorMsg);
        } else if (isNetworkAvailable(getContext())) {
            mErrorContent.setText(getString(R.string.net_error));
        } else {
            mErrorContent.setText(getString(R.string.no_net_error));
        }
        mErrorRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable(mActivity.getApplicationContext())) {
                    hideErrorView();
                } else {
                    toast(R.string.no_network);
                }
            }
        });
    }

    protected void hideErrorView() {
        rootFragmentView.removeView(mErrorView);
        initData(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mPresenter)
            mPresenter.detachView();
    }

    protected abstract P createPresenter();

    protected abstract void initView(View rootView, @Nullable Bundle savedInstanceState);

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected abstract void onFragmentResume();

    protected abstract void onFragmentPause();

    protected final void toast(int resid) {
        SingletonToast.getInstance(mActivity.getApplicationContext()).show(resid);
    }

    protected final void toast(int resid, int duration) {
        SingletonToast.getInstance(mActivity.getApplicationContext()).show(resid, duration);
    }

    protected final void toast(String strText) {
        SingletonToast.getInstance(mActivity.getApplicationContext()).show(strText);
    }

    protected final void toast(String strText, int duration) {
        SingletonToast.getInstance(mActivity.getApplicationContext()).show(strText, duration);
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

    // 获得屏幕高宽
    @NonNull
    public static Point getScreenPoint(@NonNull Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        @SuppressWarnings("deprecation")
        Point point = new Point(display.getWidth(), display.getHeight());
        return point;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        final DisplayMetrics dm = mActivity.getResources().getDisplayMetrics();
        final float scale = dm.density;
        return (int) (dpValue * scale + 0.5f);
    }

}
