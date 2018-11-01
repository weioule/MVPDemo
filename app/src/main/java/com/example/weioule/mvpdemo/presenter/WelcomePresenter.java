package com.example.weioule.mvpdemo.presenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.weioule.mvpdemo.R;
import com.example.weioule.mvpdemo.base.BasePresenter;
import com.example.weioule.mvpdemo.bean.AdBean;
import com.example.weioule.mvpdemo.base.Callback;
import com.example.weioule.mvpdemo.model.WelcomeModel;
import com.example.weioule.mvpdemo.view.WelcomeMvpView;


/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class WelcomePresenter extends BasePresenter<WelcomeMvpView, WelcomeModel> implements Callback {

    private AdBean mBean;

    public void getAdInfo() {
        getMvpModel().getAdInfo();
    }

    @Override
    public void onSuccess(Object result) {
        mBean = (AdBean) result;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ad);
        getMvpView().showAdFormCache(bitmap, mBean.getDuration());
    }

    @Override
    public void onFailure(String errorMsg) {

    }

    public void jumpToAd() {
        if (null == mBean) return;
        Uri uri = Uri.parse("https://www.baidu.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    @Override
    public WelcomeModel createModel() {
        return new WelcomeModel(this);
    }
}
