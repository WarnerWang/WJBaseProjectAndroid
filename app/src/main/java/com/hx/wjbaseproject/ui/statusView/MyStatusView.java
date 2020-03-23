package com.hx.wjbaseproject.ui.statusView;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.hx.wjbaseproject.R;
import com.hx.wjbaseproject.util.ScreenUtils;
import com.hx.wjbaseproject.util.StringUtils;

public class MyStatusView extends StatusView {

    public onRetryClickLister retryClickLister;
    private Context mContext;
    private String mEmptyText;
    private String mErrorText;

    public MyStatusView(Context context) {
        super(context);
        this.mContext = context;
    }

    public static MyStatusView getInstance(Context context, onRetryClickLister retryClickLister) {
        return getInstance(context,"暂无数据",retryClickLister);
    }

    public static MyStatusView getInstance(Context context, String emptyText, onRetryClickLister retryClickLister) {
        MyStatusView statusView = new MyStatusView(context);
        statusView.setEmptyText(emptyText);
        statusView.setRetryClickLister(retryClickLister);
        return statusView;
    }

    @Override
    public int getRetryViewLayoutId() {
//        return R.layout.item_layout_retry;
        return 0;
    }

    @Override
    public int getLoadingViewLayoutId() {
//        return R.layout.fragment_big_data_analyzing;
        return 0;
    }

    @Override
    public int getEmptyViewLayoutId() {
//        return R.layout.item_layout_empty_view;
        return 0;
    }

    @Override
    public int getSettingViewLayoutId() {
        return 0;
    }

    @Override
    public void initLoadingView() {


    }

    @Override
    public void initRetryView() {

//        mRetryView.findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (retryClickLister != null) {
//                    retryClickLister.onRetryClick();
//                }
//            }
//        });
//
//        mRetryView.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mContext instanceof Activity) {
//                    ((Activity)mContext).finish();
//                }
//            }
//        });
    }

    @Override
    public void initSettingView() {

    }

    @Override
    public void initEmptyView() {
//        if (!StringUtils.isEmpty(mEmptyText)) {
//            TextView textView = mEmptyView.findViewById(R.id.empty_text);
//            textView.setText(mEmptyText);
//        }
    }

    @Override
    public void startLoading() {
//        ImageView ivBall = mLoadingView.findViewById(R.id.iv_ball);
//        ivBall.clearAnimation();
//        TranslateAnimation translateAnimation = new TranslateAnimation(
//                ivBall.getX(),
//                ivBall.getX(),
//                ivBall.getY(),
//                ivBall.getY() - ScreenUtils.dpToPx(mContext, 50));
//        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
//        translateAnimation.setDuration(600);
//        translateAnimation.setRepeatCount(Integer.MAX_VALUE);
//        translateAnimation.setRepeatMode(Animation.REVERSE);
//        translateAnimation.start();
//        ivBall.setAnimation(translateAnimation);

    }

    public MyStatusView setRetryClickLister(onRetryClickLister retryClickLister) {
        this.retryClickLister = retryClickLister;
        return this;
    }

    public MyStatusView setEmptyText(String emptyText) {
        mEmptyText = emptyText;
        return this;
    }

    public MyStatusView setErrorText(String errorText) {
//        mErrorText = errorText;
//        TextView textView = mRetryView.findViewById(R.id.error_msg);
//        textView.setText(errorText);
        return this;
    }

    public interface onRetryClickLister {
        void onRetryClick();
    }


}
