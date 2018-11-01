package com.example.weioule.mvpdemo.base;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public interface Callback {
    /**
     * 成功
     *
     * @param result
     */
    void onSuccess(/*int id, int type,*/ Object result);

    /**
     * 失败
     *
     * @param errorMsg 失败信息
     */
    void onFailure(String errorMsg);
}
