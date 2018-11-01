package com.example.weioule.mvpdemo.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weioule.mvpdemo.R;
import com.example.weioule.mvpdemo.base.RecyclerViewDivider;
import com.example.weioule.mvpdemo.widget.TextFooterView;
import com.example.weioule.mvpdemo.base.BaseFragment;
import com.example.weioule.mvpdemo.adapter.HomeFragmentAdapter;
import com.example.weioule.mvpdemo.bean.HomeDataBean;
import com.example.weioule.mvpdemo.model.HomeFragmentModel;
import com.example.weioule.mvpdemo.presenter.HomeFragmentPresenter;
import com.example.weioule.mvpdemo.view.HomeFragmentMvpView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
@SuppressLint("ValidFragment")
public class HomeFragment extends BaseFragment<HomeFragmentMvpView, HomeFragmentModel, HomeFragmentPresenter> implements HomeFragmentMvpView {

    private TwinklingRefreshLayout mRefreshLayout;
    private HomeFragmentAdapter mAdapter;
    private List<HomeDataBean> mDataBeanList;
    private RecyclerView mRecyclerView;
    private final String type;
    private int pageNo = 1;

    public HomeFragment(String type) {
        this.type = type;
    }

    @Override
    public void initView(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        mRecyclerView = rootView.findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.addItemDecoration(new RecyclerViewDivider());
        mRefreshLayout = rootView.findViewById(R.id.refresh_home);
        mRefreshLayout.setEnableOverScroll(false);
        mRefreshLayout.setHeaderView(new SinaRefreshView(mActivity));
        mRefreshLayout.setBottomView(new TextFooterView(mActivity));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {

            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
                mRefreshLayout.setEnableLoadmore(true);
            }

            @Override
            public void onFinishLoadMore() {
                super.onFinishLoadMore();
                mRefreshLayout.setEnableRefresh(true);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mRefreshLayout.setEnableLoadmore(false);
                pageNo = 1;
                mPresenter.loadDatas(pageNo, type);
                mAdapter.removeAllFooterView();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mRefreshLayout.setEnableRefresh(false);
                mPresenter.loadDatas(pageNo, type);
            }
        });
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mDataBeanList = new ArrayList<>();
        mAdapter = new HomeFragmentAdapter(mDataBeanList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.loadDatas(pageNo, type);
            }
        }, 1000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void onFragmentResume() {
    }

    @Override
    protected void onFragmentPause() {
    }

    @Override
    public Context getContexts() {
        return mActivity;
    }

    @Override
    protected HomeFragmentPresenter createPresenter() {
        return new HomeFragmentPresenter();
    }

    @Override
    public void setDatas(List<HomeDataBean> result) {
        if (pageNo == 1 && null != mDataBeanList) {
            mDataBeanList.clear();
        }
        mAdapter.addAll(result);
        if (pageNo == 1) {
            mRefreshLayout.finishRefreshing();
            //若不满一页则不让加载更多
            mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (checkIsVisible(mRecyclerView.getLayoutManager().findViewByPosition(mDataBeanList.size() - 1))) {
                        mRefreshLayout.setEnableLoadmore(false);
                    } else {
                        mRefreshLayout.setEnableLoadmore(true);
                    }
                }
            });
        } else {
            mRefreshLayout.finishLoadmore();
        }
        pageNo++;
    }

    @Override
    public void showEmptyView(String errorMsg) {
        mRefreshLayout.finishRefreshing();
        showErrorView();
        mErrorContent.setText(errorMsg);
    }

    @Override
    public void showFooterView() {
        TextView view = new TextView(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(30));
        view.setLayoutParams(params);
        view.setText("我是有底线的");
        view.setGravity(Gravity.CENTER);
        mAdapter.addFooterView(view);
        mRefreshLayout.finishLoadmore();
        mRefreshLayout.setEnableLoadmore(false);
    }

    public Boolean checkIsVisible(View view) {
        if (view == null) {
            return false;
        }
        Point screenPoint = getScreenPoint(mActivity);
        int screenWidth = screenPoint.x;
        int screenHeight = screenPoint.y;
        Rect rect = new Rect(0, 0, screenWidth, screenHeight);
        int[] location = new int[2];
        view.getLocationInWindow(location);
        if (view.getGlobalVisibleRect(rect)) {
            return true;
        } else {
            //view已不在屏幕可见区域;
            return false;
        }
    }

}
