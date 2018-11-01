package com.example.weioule.mvpdemo.base;

import com.example.weioule.mvpdemo.bean.AdBean;
import com.example.weioule.mvpdemo.bean.HomeDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/30.
 */
public class HttpRequest {

    public static void doRequest(Class<?> cls, HttpRequestListener listener) {
        if (null != listener) {
            try {
                //此处模拟gson解析
                AdBean bean = (AdBean) cls.newInstance();
                bean.setImagUrl("");
                bean.setJumpUrl("https://www.baidu.com");
                bean.setDuration(3);
                listener.onSuccess(bean);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void doRequest(int pageNo, int pageSize, String type, Class<?> cls, HttpRequestListener listener) {
        if (null != listener) {
            if (pageNo > 10) {
                listener.onFailed(new Exception("无更多数据"));
            } else {
                listener.onSuccess(addBean(pageNo, pageSize, type, cls));
            }
        }
    }

    private static List<Object> addBean(int pageNo, int pageSize, String type, Class<?> cls) {
        //模拟数据返回
        ArrayList<Object> list = new ArrayList<>();
        for (int i = pageSize * (pageNo - 1); i < pageSize * pageNo; i++) {
            try {
                HomeDataBean bean = (HomeDataBean) cls.newInstance();
                bean.setContent(type + " ------- item " + i);
                list.add(bean);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public interface HttpRequestListener {

        void onFailed(/*int id, int type,*/ Exception e);

        void onSuccess(/*int id, int type, */Object result);
    }
}
