package com.example.weioule.mvpdemo.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Author by weioule.
 * Date on 2018/8/17.
 */
public abstract class BaseRecylerViewAdapter<E> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final int FOOTER_VIEW = 0x00000111;
    protected static final int HEADER_VIEW = 0x00000112;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnItemChildClickListener mOnItemChildClickListener;
    private OnItemChildLongClickListener mOnItemChildLongClickListener;

    protected Context mContext;
    protected List<E> mDataLists;
    protected LinearLayout mFooterLayout;
    protected LinearLayout mHeaderLayout;

    public BaseRecylerViewAdapter(List<E> mDataLists) {
        if (null == mDataLists) {
            this.mDataLists = new ArrayList<>();
        } else {
            this.mDataLists = mDataLists;
        }
    }

    @Override
    public int getItemCount() {
        return mDataLists == null ? 0 : getHeaderLayoutCount() + mDataLists.size() + getFooterLayoutCount();
    }

    @Override
    public int getItemViewType(int position) {

        int numHeaders = getHeaderLayoutCount();
        if (position < numHeaders) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position - numHeaders;
            int adapterCount = mDataLists.size();
            if (adjPosition < adapterCount) {
                return getMultipleItemViewType(adjPosition);
            } else {
                adjPosition = adjPosition - adapterCount;
                int numFooters = getFooterLayoutCount();
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                }
            }
        }

        return -1;
    }


    protected int getMultipleItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view;
        switch (viewType) {
            case FOOTER_VIEW:
                view = mFooterLayout;
                break;
            case HEADER_VIEW:
                view = mHeaderLayout;
                break;
            default:
                view = LayoutInflater.from(mContext).inflate(getLayout(viewType), parent, false);
                break;
        }

        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int position) {

        int viewType = viewHolder.getItemViewType();

        switch (viewType) {
            case HEADER_VIEW:
            case FOOTER_VIEW:
                break;
            default:
                if (mDataLists != null && mDataLists.size() > 0) {
                    E e = mDataLists.get(position - getHeaderLayoutCount());
                    if (null != e) {
                        bindData(viewHolder, e, position);
                    }
                }
                break;
        }
    }

    protected abstract int getLayout(int viewType);

    protected abstract void bindData(BaseViewHolder holder, E data, int position);

    public E getItemData(int position) {
        return mDataLists.get(position);
    }

    public void addItem(E item) {
        addItem(-1, item);
    }

    public void addItem(int position, E item) {
        synchronized (BaseRecylerViewAdapter.class) {
            if (mDataLists == null) {
                mDataLists = new ArrayList<E>();
            }

            if (position < 0 || position > mDataLists.size()) {
                mDataLists.add(item);
            } else {
                mDataLists.add(position, item);
            }
            notifyItemInserted(position);
        }
    }

    public void addAll(List<E> lists) {
        synchronized (BaseRecylerViewAdapter.class) {
            if (null == mDataLists) {
                mDataLists = lists;
            } else {
                mDataLists.addAll(lists);
            }

            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        synchronized (BaseRecylerViewAdapter.class) {
            if (mDataLists != null && position >= 0 && mDataLists.size() > 0 && mDataLists.size() > position) {
                mDataLists.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public void replace(List<E> lists) {
        synchronized (BaseRecylerViewAdapter.class) {
            mDataLists = lists;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        synchronized (BaseRecylerViewAdapter.class) {
            if (mDataLists != null) {
                mDataLists.clear();
                notifyDataSetChanged();
            }
        }
    }

    private int getHeaderViewPosition() {
        return 0;
    }

    public LinearLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    public int getHeaderLayoutCount() {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    public int addHeaderView(View header) {
        return addHeaderView(header, -1);
    }

    public int addHeaderView(View header, int index) {
        return addHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int addHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(header.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mHeaderLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mHeaderLayout.addView(header, index);
        if (mHeaderLayout.getChildCount() == 1) {
            notifyItemInserted(getHeaderViewPosition());
        }
        return index;
    }

    public int setHeaderView(View header) {
        return setHeaderView(header, 0);
    }

    public int setHeaderView(View header, int index) {
        return setHeaderView(header, index, LinearLayout.VERTICAL);
    }

    public int setHeaderView(View header, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeaderView(header, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(header, index);
            return index;
        }
    }

    public void removeHeaderView(View header) {
        if (getHeaderLayoutCount() == 0 || mHeaderLayout.getChildCount() <= 0) return;

        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() <= 0) {
            notifyItemRemoved(getHeaderViewPosition());
        }
    }

    public void removeAllHeaderView() {
        if (getHeaderLayoutCount() <= 0 || mHeaderLayout.getChildCount() <= 0) return;

        mHeaderLayout.removeAllViews();
        notifyItemRemoved(getHeaderViewPosition());
    }

    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    public int getFooterLayoutCount() {
        if (mFooterLayout == null || mFooterLayout.getChildCount() == 0) {
            return 0;
        }
        return 1;
    }

    private int getFooterViewPosition() {
        return getHeaderLayoutCount() + mDataLists.size();
    }

    public int addFooterView(View footer) {
        return addFooterView(footer, -1);
    }

    public int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        if (mFooterLayout.getChildCount() == 1) {
            notifyItemInserted(getFooterViewPosition());
        }
        return index;
    }

    public int setFooterView(View header) {
        return setFooterView(header, 0);
    }

    public int setFooterView(View header, int index) {
        return setFooterView(header, index, LinearLayout.VERTICAL);
    }

    public int setFooterView(View header, int index, int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            return addFooterView(header, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(header, index);
            return index;
        }
    }

    public void removeFooterView(View footer) {
        if (getFooterLayoutCount() == 0 || mFooterLayout.getChildCount() <= 0) return;

        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() <= 0) {
            notifyItemRemoved(getFooterViewPosition());
        }
    }

    public void removeAllFooterView() {
        if (getFooterLayoutCount() == 0 || mFooterLayout.getChildCount() <= 0) return;

        mFooterLayout.removeAllViews();
        notifyItemRemoved(getFooterViewPosition());
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListener = listener;
    }

    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        mOnItemChildClickListener = listener;
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        mOnItemChildLongClickListener = listener;
    }

    public final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    public final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Nullable
    public final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    @Nullable
    public final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(BaseRecylerViewAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(BaseRecylerViewAdapter adapter, View view, int position);
    }

    public interface OnItemChildClickListener {
        void onItemChildClick(BaseRecylerViewAdapter adapter, View view, int position);
    }

    public interface OnItemChildLongClickListener {
        boolean onItemChildLongClick(BaseRecylerViewAdapter adapter, View view, int position);
    }

}
