package com.hx.wjbaseproject.ui.statusView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class StatusView {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected View mLoadingView;// 正在加载界面
    protected View mRetryView;// 返回数据错误
    protected View mSettingView;// 一般是无网络状态,需要去设置
    protected View mEmptyView;// 返回数据是0个
    protected View mNeedVipView;//需要普通会员
    protected View mNeedSVipView;//需要至尊会员
    protected View mOddsSettingView;//盘赔模型设置

    public StatusView(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (checkResourceID(getLoadingViewLayoutId())) {
            mLoadingView = mInflater.inflate(getLoadingViewLayoutId(),
                    null);
        }
        if (checkResourceID(getRetryViewLayoutId())) {
            mRetryView = mInflater.inflate(getRetryViewLayoutId(), null);
        }
        if (checkResourceID(getSettingViewLayoutId())) {
            mSettingView = mInflater.inflate(getSettingViewLayoutId(), null);
        }
        if (checkResourceID(getEmptyViewLayoutId())) {
            mEmptyView = mInflater.inflate(getEmptyViewLayoutId(), null);
        }

        initLoadingView();
        initRetryView();
        initSettingView();
        initEmptyView();
    }

    public View getRetryView() {
        return mRetryView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getSettingView() {
        return mSettingView;
    }


    public abstract int getRetryViewLayoutId();

    public abstract int getLoadingViewLayoutId();

    public abstract int getEmptyViewLayoutId();

    public abstract int getSettingViewLayoutId();

    public abstract void initLoadingView();

    public abstract void initRetryView();

    public abstract void initSettingView();

    public abstract void initEmptyView();

    public abstract void startLoading();

    private boolean checkResourceID(int layoutId) {
        return (layoutId >>> 24) >= 2;
    }
}
