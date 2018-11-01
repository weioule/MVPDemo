package com.example.weioule.mvpdemo.base;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author by weioule.
 * Date on 2018/8/17.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> viewList;

    public BaseViewHolder(View itemView) {
        super(itemView);
        viewList = new SparseArray<>();
    }

    public View getView(int viewId) {
        View view = viewList.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewList.put(viewId, view);
        }
        return view;
    }

    public BaseViewHolder setOnClickListener(int viewId, View.OnClickListener mLis) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(mLis);
        }
        return this;
    }

    public BaseViewHolder setText(int viewId, String text) {
        TextView view = (TextView) getView(viewId);
        view.setText(text);
        return this;
    }

    public BaseViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = (ImageView) getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }
}
