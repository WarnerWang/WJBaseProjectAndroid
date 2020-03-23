package com.hx.wjbaseproject.ui.base;

public interface BasePresenter<T extends BaseView> {

    void addView(T view);

    void removeView();

}
