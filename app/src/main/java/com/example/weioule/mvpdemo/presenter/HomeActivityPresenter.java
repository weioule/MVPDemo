package com.example.weioule.mvpdemo.presenter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.weioule.mvpdemo.base.BasePresenter;
import com.example.weioule.mvpdemo.base.Callback;
import com.example.weioule.mvpdemo.model.HomeActivityModel;
import com.example.weioule.mvpdemo.view.HomeActivityMvpView;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class HomeActivityPresenter extends BasePresenter<HomeActivityMvpView, HomeActivityModel> implements Callback {

    public void checkVersion() {
        showLoadingDialog();
        getMvpModel().checkVersion();
    }

    @Override
    public void onSuccess(Object result) {
        hideLoadingDialog();
        getMvpView().checkVersion((Boolean) result);
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void showUpdateDialog(Context context) {
        new AlertDialog.Builder(context)
                .setMessage("有新版本啦？")
                .setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getMvpView().upgrade();
                    }
                })
                .setNeutralButton("稍后提醒", null)
                .create().show();
    }

    @Override
    public HomeActivityModel createModel() {
        return new HomeActivityModel(this);
    }
}
