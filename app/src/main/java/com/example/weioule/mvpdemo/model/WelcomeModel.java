package com.example.weioule.mvpdemo.model;


import com.example.weioule.mvpdemo.base.HttpRequest;
import com.example.weioule.mvpdemo.base.BaseModel;
import com.example.weioule.mvpdemo.base.Callback;
import com.example.weioule.mvpdemo.bean.AdBean;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class WelcomeModel extends BaseModel implements HttpRequest.HttpRequestListener {

    public WelcomeModel(Callback callback) {
        super(callback);
    }

    public void getAdInfo() {
        HttpRequest.doRequest(AdBean.class, this);
    }

    @Override
    public void onSuccess(Object result) {
        callback.onSuccess(result);
    }

    @Override
    public void onFailed(Exception e) {

    }
}