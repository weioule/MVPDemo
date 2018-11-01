package com.example.weioule.mvpdemo.base;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Author by weioule.
 * Date on 2018/8/17.
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration {

    private final static int CLR_DEFAULT_LINE = 0xff9c9c9c;

    private int mOrientation;
    private int mDividerHight;
    private Paint mColorPaint;
    private int spanCount;

    public RecyclerViewDivider() {
        this(1);
    }

    public RecyclerViewDivider(int dividerHight) {
        this(dividerHight, CLR_DEFAULT_LINE);
    }

    public RecyclerViewDivider(int dividerHight, int dividerColor) {
        this(dividerHight, dividerColor, 0);
    }

    /**
     * int dividerHight 分割线的线宽
     * int dividerColor 分割线的颜色
     * int mOrientation 分割线的方向
     */
    public RecyclerViewDivider(int dividerHight, int dividerColor, int orientation) {
        this.mDividerHight = dividerHight;
        this.mOrientation = orientation;
        mColorPaint = new Paint();
        mColorPaint.setColor(dividerColor);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //列数
        spanCount = getSpanCount(parent);
        //画水平和垂直分割线
        switch (mOrientation) {
            case LinearLayout.VERTICAL:
                drawVerticalDivider(c, parent);
                break;
            case LinearLayout.HORIZONTAL:
                drawHorizontalDivider(c, parent);
                break;
            default:
                drawHorizontalDivider(c, parent);
                drawVerticalDivider(c, parent);
                break;
        }

    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager)
                    .getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontalDivider(Canvas c, RecyclerView parent) {
        float childCount = parent.getChildCount();
        int lieCount = (int) Math.ceil(childCount / spanCount);//行数

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHight;

            //最后一行就不画了
            if (i >= (lieCount - 1) * spanCount) {
                continue;
            }

            c.drawRect(left, top, right, bottom, mColorPaint);
        }
    }

    public void drawVerticalDivider(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHight;

            //最后一列就不画了
            if (i % spanCount == spanCount - 1) {
                continue;
            }

            c.drawRect(left, top, right, bottom, mColorPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, 0, 0, mDividerHight);
    }


}