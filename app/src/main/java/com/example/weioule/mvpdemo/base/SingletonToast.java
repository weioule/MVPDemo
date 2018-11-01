package com.example.weioule.mvpdemo.base;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Author by weioule.
 * Date on 2018/9/5.
 */
public class SingletonToast {

    private Toast mToast;
    private int mResIdLast;
    private String mTextLast;
    private long mShowTimeLast;
    private static Context mContext;
    private static SingletonToast instance;

    private SingletonToast() {
    }

    public static SingletonToast getInstance(Context context) {
        if (null == instance) {
            instance = new SingletonToast();
        }
        if (null == context) {
            mContext = context;
        }
        return instance;
    }

    public void show(int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public void show(int resId, int duration) {
        if (resId <= 0) {
            return;
        }

        if (resId == mResIdLast && System.currentTimeMillis() - mShowTimeLast < 2000) {
            return;
        }

        if (null == mToast) {
            mToast = Toast.makeText(mContext, resId, duration);
        } else {
            mToast.setText(resId);
            mToast.setDuration(duration);
        }

        mToast.show();
        mShowTimeLast = System.currentTimeMillis();
        mResIdLast = resId;
    }

    public void show(String strText) {
        show(strText, Toast.LENGTH_SHORT);
    }

    public void show(String strText, int duration) {
        if (TextUtils.isEmpty(strText)) {
            return;
        }

        if (strText.equals(mTextLast) && System.currentTimeMillis() - mShowTimeLast < 2000) {
            return;
        }

        if (strText.length() > 100) {
            strText = strText.substring(0, 100) + "...";
        }

        if (null == mToast) {
            mToast = Toast.makeText(mContext, strText, duration);
        } else {
            mToast.setText(strText);
            mToast.setDuration(duration);
        }

        mToast.show();
        mShowTimeLast = System.currentTimeMillis();
        mTextLast = strText;
    }
}
