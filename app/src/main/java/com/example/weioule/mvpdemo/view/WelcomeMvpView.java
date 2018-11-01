package com.example.weioule.mvpdemo.view;

import android.graphics.Bitmap;

import com.example.weioule.mvpdemo.base.IView;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public interface WelcomeMvpView extends IView {

    void showAdFormCache(Bitmap bitmap, int duration);
}
