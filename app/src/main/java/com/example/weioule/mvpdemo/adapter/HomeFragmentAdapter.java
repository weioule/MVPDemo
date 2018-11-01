package com.example.weioule.mvpdemo.adapter;

import com.example.weioule.mvpdemo.base.BaseRecylerViewAdapter;
import com.example.weioule.mvpdemo.base.BaseViewHolder;
import com.example.weioule.mvpdemo.R;
import com.example.weioule.mvpdemo.bean.HomeDataBean;

import java.util.List;

/**
 * Author by weioule.
 * Date on 2018/10/30.
 */
public class HomeFragmentAdapter extends BaseRecylerViewAdapter<HomeDataBean> {

    public HomeFragmentAdapter(List<HomeDataBean> mDataLists) {
        super(mDataLists);
    }

    @Override
    protected int getLayout(int viewType) {
        return R.layout.home_item_layout;
    }

    @Override
    protected void bindData(BaseViewHolder holder, HomeDataBean data, int position) {
        holder.setText(R.id.tv, mDataLists.get(position).getContent());
    }
}
