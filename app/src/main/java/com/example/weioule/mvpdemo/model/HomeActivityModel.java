package com.example.weioule.mvpdemo.model;


import com.example.weioule.mvpdemo.base.HttpRequest;
import com.example.weioule.mvpdemo.base.BaseModel;
import com.example.weioule.mvpdemo.base.Callback;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeActivityModel extends BaseModel implements HttpRequest.HttpRequestListener {


    public HomeActivityModel(Callback callback) {
        super(callback);
    }

    public void checkVersion() {
        onSuccess(null);
    }

    @Override
    public void onFailed(Exception e) {

    }

    @Override
    public void onSuccess(Object result) {
        callback.onSuccess(true);
    }
}
