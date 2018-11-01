package com.example.weioule.mvpdemo.view;

import com.example.weioule.mvpdemo.base.IView;
import com.example.weioule.mvpdemo.bean.HomeDataBean;

import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public interface HomeFragmentMvpView extends IView {
    void setDatas(List<HomeDataBean> result);

    void showEmptyView(String errorMsg);

    void showFooterView();
}
