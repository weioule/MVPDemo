package com.example.weioule.mvpdemo.uitls;

import android.util.Log;

/**
 * @author weioule
 * @date 2019/8/3.
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int ASSERT = 6;
    public static final int NOTHING = 7;
    public static int LEVEL = INFO;

    private static final String TAG = "LogUtil";

    //可以在Application里统一与测试环境绑定，正式环境不打印log
    public static void setLogLevel(int level) {
        LEVEL = level;
    }

    public static void v(String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String msg) {
        if (LEVEL <= INFO) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String msg) {
        if (LEVEL <= WARN) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String msg) {
        if (LEVEL <= ERROR) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }

    public static void a(String msg) {
        if (LEVEL <= ASSERT) {
            Log.println(Log.ASSERT, TAG, msg);
        }
    }

    public static void a(String tag, String msg) {
        if (LEVEL <= ASSERT) {
            Log.println(Log.ASSERT, tag, msg);
        }
    }

}