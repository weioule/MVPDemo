package com.example.weioule.mvpdemo.presenter;


import com.example.weioule.mvpdemo.base.SingletonToast;
import com.example.weioule.mvpdemo.base.BasePresenter;
import com.example.weioule.mvpdemo.bean.HomeDataBean;
import com.example.weioule.mvpdemo.base.Callback;
import com.example.weioule.mvpdemo.model.HomeFragmentModel;
import com.example.weioule.mvpdemo.view.HomeFragmentMvpView;

import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeFragmentPresenter extends BasePresenter<HomeFragmentMvpView, HomeFragmentModel> implements Callback {

    private int pageNo;

    public void loadDatas(int pageNo, String type) {
        getMvpModel().getDatas(pageNo, type);
        this.pageNo = pageNo;
    }

    @Override
    public void onSuccess(Object result) {
        if (null == result || ((List) result).size() <= 0) {
            if (pageNo == 1) {
                getMvpView().showEmptyView("暂无数据");
            } else {
                getMvpView().showFooterView();
            }
        } else {
            getMvpView().setDatas((List<HomeDataBean>) result);
        }
    }

    @Override
    public void onFailure(String errorMsg) {
        if (1 == pageNo) {
            getMvpView().showEmptyView(errorMsg);
            SingletonToast.getInstance(mContext).show(errorMsg);
        } else {
            getMvpView().showFooterView();
        }
    }

    @Override
    public HomeFragmentModel createModel() {
        return new HomeFragmentModel(this);
    }
}
