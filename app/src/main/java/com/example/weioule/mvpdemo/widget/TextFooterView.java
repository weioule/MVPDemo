package com.example.weioule.mvpdemo.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.weioule.mvpdemo.R;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;

/**
 * Author by weioule.
 * Date on 2018/10/29.
 */
public class TextFooterView extends LinearLayout implements IBottomView {

    private View rootView;
    private Context mContext;
    private ImageView iv_loading;
    private TextView tv_footer;
    private String footerText;

    public TextFooterView(Context context) {
        this(context, null);
    }

    public TextFooterView(Context context, String text) {
        this(context, text, null);
    }

    public TextFooterView(Context context, String text, @Nullable AttributeSet attrs) {
        this(context, text, attrs, 0);
    }

    public TextFooterView(Context context, String text, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.footerText = text;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onPullingUp(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void startAnim(float maxBottomHeight, float bottomHeight) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(
                iv_loading, "rotation", 0f, 360f);
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(10);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.start();
    }

    @Override
    public void onPullReleasing(float fraction, float maxBottomHeight, float bottomHeight) {

    }

    @Override
    public void onFinish() {

    }

    @Override
    public void reset() {

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (rootView == null) {
            rootView = View.inflate(getContext(), R.layout.recyclerview_footer_text, null);
            iv_loading = rootView.findViewById(R.id.footer_loading);
            tv_footer = rootView.findViewById(R.id.footer_text);
            if(!TextUtils.isEmpty(footerText))
                tv_footer.setText(footerText);
            int size = DensityUtil.dp2px(mContext,60);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,size);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            rootView.setLayoutParams(params);
            addView(rootView);
        }
    }
}
