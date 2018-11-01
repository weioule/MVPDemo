package com.example.weioule.mvpdemo.model;


import com.example.weioule.mvpdemo.base.HttpRequest;
import com.example.weioule.mvpdemo.base.BaseModel;
import com.example.weioule.mvpdemo.base.Callback;
import com.example.weioule.mvpdemo.bean.HomeDataBean;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeFragmentModel extends BaseModel implements HttpRequest.HttpRequestListener {

    private static int pageSize = 15;

    public HomeFragmentModel(Callback callback) {
        super(callback);
    }

    public void getDatas(int pageNo, String type) {
        HttpRequest.doRequest(pageNo, pageSize, type, HomeDataBean.class, this);
    }

    @Override
    public void onFailed(/*int id, int type,*/ Exception e) {
        e.printStackTrace();
        callback.onFailure(e.getMessage());
    }

    @Override
    public void onSuccess(Object result) {
        callback.onSuccess(/*id, type,*/result);
    }
}
