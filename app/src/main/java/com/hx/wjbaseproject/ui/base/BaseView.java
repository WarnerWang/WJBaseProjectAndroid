package com.hx.wjbaseproject.ui.base;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
