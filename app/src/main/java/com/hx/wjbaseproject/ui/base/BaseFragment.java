package com.hx.wjbaseproject.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hx.wjbaseproject.util.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import rx.Subscription;

public class BaseFragment extends Fragment {

    private MaterialDialog progressDialog;
    private ArrayList<Subscription> subscriptions = new ArrayList<>();

    private boolean isVisibleToUser;

    private OnRequestPermissionResultListener onRequestPermissionResultListener;

    public interface OnRequestPermissionResultListener {
        void onRequestPermissionResultListener(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(getContext())
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        }

        progressDialog.show();
    }

    protected void dismissDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
            }
        } catch (Exception e) {
            Logger.printException(e);
        }
    }

    protected void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroyView() {
        try {
            for (Subscription subscription : subscriptions) {
                if (!subscription.isUnsubscribed()) {
                    subscription.unsubscribe();
                }
            }
            subscriptions.clear();
        } catch (Exception e) {
            Logger.printException(e);
        }

        EventBus eventBus = EventBus.getDefault();
        if (eventBus.isRegistered(this)) {
            eventBus.unregister(this);
        }
        dismissDialog();
        super.onDestroyView();
    }

    protected int getViewIdByString(String idStr, Context context) {
        int id = context.getResources().getIdentifier(idStr, "id", context.getPackageName());
        return id;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    /**
     * activity返回销毁刷新
     */
    public void onResultRefresh(int requestCode, int resultCode, Intent data) {

    }

    /**
     * 请求权限
     *
     * @param permissions 权限列表
     * @param resultCode
     * @return 是否已经具备相应权限
     */
    public boolean checkReadPermission(String[] permissions, int resultCode) {

        List<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getContext(), permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }

        if (mPermissionList.size() == 0) {
            return true;
        }

        String[] needPermissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
        ActivityCompat.requestPermissions(getActivity(), needPermissions, resultCode);

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (onRequestPermissionResultListener != null) {
            this.onRequestPermissionResultListener.onRequestPermissionResultListener(requestCode, permissions, grantResults);
        }
    }

    public void setOnRequestPermissionResultListener(OnRequestPermissionResultListener onRequestPermissionResultListener) {
        this.onRequestPermissionResultListener = onRequestPermissionResultListener;
    }
}
