package com.example.weioule.mvpdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.example.weioule.mvpdemo.R;

import java.lang.reflect.Method;

public class WebviewActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private WebView mWebview;
    private String mUrl = "https://www.baidu.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebview = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.webview_progressbar);

        WebSettings webSettings = mWebview.getSettings();
        if (mUrl.startsWith("file://")) {
            webSettings.setJavaScriptEnabled(false);
        } else
            webSettings.setJavaScriptEnabled(true);//允许使用js
        //不使用缓存，只从网络获取数据.
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        mWebview.setWebChromeClient(mWebChromeClient);
        mWebview.setWebViewClient(mWebViewClient);
        mWebview.loadUrl(mUrl);

        mWebview.removeJavascriptInterface("searchBoxJavaBridge_");
        mWebview.removeJavascriptInterface("accessibility");
        mWebview.removeJavascriptInterface("accessibilityTraversal");

        disableAccessibility(this);
    }

    @Override
    protected void onPause() {
        if (mWebview != null) {
            mWebview.onPause();
            mWebview.pauseTimers();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mWebview != null) {
            mWebview.onResume();
            mWebview.resumeTimers();
        }

        super.onResume();
    }


    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.clearHistory();
            mWebview.clearCache(true);
            mWebview.destroy();
        }

        super.onDestroy();
    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            //页面加载完成
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //页面开始加载
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        @SuppressWarnings("deprecation")
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.toLowerCase().startsWith("http")) {
                view.loadUrl(url);
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

    };

    //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
        @Override
        public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
            localBuilder.setMessage(message).setPositiveButton(getString(R.string.ok), null);
            localBuilder.setCancelable(false);
            localBuilder.create().show();

            //注意:
            //必须要这一句代码:result.confirm()表示:
            //处理结果为确定状态同时唤醒WebCore线程
            //否则不能继续点击按钮
            result.confirm();
            return true;
        }

        //获取网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        //加载进度回调
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    };

    public static void disableAccessibility(Context context) {
        if (Build.VERSION.SDK_INT == 17/*4.2 (Build.VERSION_CODES.JELLY_BEAN_MR1)*/) {
            if (context != null) {
                try {
                    AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
                    if (!am.isEnabled()) {
                        //Not need to disable accessibility
                        return;
                    }
                    Method setState = am.getClass().getDeclaredMethod("setState", int.class);
                    setState.setAccessible(true);
                    setState.invoke(am, 0);/**{@link AccessibilityManager#STATE_FLAG_ACCESSIBILITY_ENABLED}*/
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebview.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            //点击返回按钮的时候判断有没有上一页
            mWebview.goBack();
            // goBack()表示返回webView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
