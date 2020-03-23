package com.hx.wjbaseproject.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.hx.wjbaseproject.R;
import com.hx.wjbaseproject.ui.base.BaseActivity;
import com.hx.wjbaseproject.ui.test.TestFragment;
import com.hx.wjbaseproject.util.Logger;
import com.hx.wjbaseproject.util.ToastUtil;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String KEY_PAGE_OFFSET = "PAGE_OFFSET";
    @BindView(R.id.home_view_pager)
    ViewPager viewPager;
    @BindView(R.id.bottom_tab_layout)
    TabLayout bottomTabLayout;

    private final int[] iconArray = {
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher};
    private final int[] iconPressedArray = {
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round};
    private final String[] tabTextArray = {
            "页面0",
            "页面1",
            "页面2",
            "页面3"};


    private long mPressedTime = 0;

    public static void startMe(Context context, int pageOffset) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KEY_PAGE_OFFSET, pageOffset);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (context instanceof Activity) {

        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);

    }

    public static void startMe(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        transparentStatusBar(true);
        initViewPager();
        checkPermission();
    }

    private void checkPermission(){
        Activity activity = this;
        if (!checkReadPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, 0)) {

            this.setOnRequestPermissionResultListener(new OnRequestPermissionResultListener() {
                @Override
                public void onRequestPermissionResultListener(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                    boolean canStart = true;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            ToastUtil.ins().show("应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。");
                            if (permissions[i].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                canStart = false;
//                                ToastUtil.ins().show("分享失败，请确保应用读写存储权限开启", true);
                            }
                        }else {
                            /**
                             * 对于适配了Android6.0以上(API >= 23)的App，
                             * 建议开发者在获得了动态权限之后，调用SDK的初始化代码，
                             * 否则SDK可能受影响。这是因为SDK需要获得android.permission.READPHONESTATE权限，
                             * 来读取 IMEI作为用户标识。如若权限添加⽆法在SDK初始化之前完成，可以在初始化过程中进行添加。
                             */
                            if (permissions[i].equals(Manifest.permission.READ_PHONE_STATE)) {
//                                LaunchActivity.initTouTiao(activity);
                            }
                        }
                    }
                }
            });
        }else {
        }
    }

    public void initViewPager(){
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 修改状态栏字体颜色
                if (tab.getPosition() == 3) {
                    setStatusBarDarkFont(true);
                } else {
                    setStatusBarDarkFont(false);
                }

                viewPager.setCurrentItem(tab.getPosition());

                //改变Tab 状态
                for (int i = 0; i < bottomTabLayout.getTabCount(); i++) {
                    TabLayout.Tab tabAt = bottomTabLayout.getTabAt(i);
                    if (tabAt == null) {
                        Logger.e("tab is null");
                        return;
                    }

                    View customView = tabAt.getCustomView();
                    if (customView == null) {
                        return;
                    }

                    ImageView imageView = customView.findViewById(R.id.iv_icon);
                    TextView textView = customView.findViewById(R.id.tv_text);

                    if (i == tab.getPosition()) {
                        imageView.setImageResource(iconPressedArray[i]);
                        textView.setTextColor(Color.parseColor("#F5A623"));
                    } else {
                        imageView.setImageResource(iconArray[i]);
                        textView.setTextColor(Color.parseColor("#9EA4BA"));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < iconArray.length; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_layout_main_tab, null);
            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            ivIcon.setImageResource(iconArray[i]);
            TextView tvText = view.findViewById(R.id.tv_text);
            tvText.setText(tabTextArray[i]);
            bottomTabLayout.addTab(
                    bottomTabLayout.newTab().setCustomView(view)
            );
        }
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(), bottomTabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomTabLayout));
        viewPager.setOffscreenPageLimit(bottomTabLayout.getTabCount() - 1);

        viewPager.setCurrentItem(1);
        int pageOffset = getIntent().getIntExtra(KEY_PAGE_OFFSET, 0);
        if (pageOffset >= 0 && pageOffset < iconArray.length) {
            viewPager.setCurrentItem(pageOffset);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int pageOffset = intent.getIntExtra(KEY_PAGE_OFFSET, 0);
        if (pageOffset >= 0 && pageOffset < iconArray.length) {
            viewPager.setCurrentItem(pageOffset);
        }
    }

    public void setCurrentPage(int pageOffset) {
        if (pageOffset >= 0 && pageOffset < iconArray.length) {
            viewPager.setCurrentItem(pageOffset);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if ((mNowTime - mPressedTime) > 2000) {//比较两次按键时间差
            ToastUtil.ins().show("再按一次退出程序");
            mPressedTime = mNowTime;
        } else {//退出程序
            this.finish();
            System.exit(0);
        }
    }


    public static class PageAdapter extends FragmentPagerAdapter {

        private int num;
        public HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

        PageAdapter(FragmentManager fm, int num) {
            super(fm);
            this.num = num;
        }

        @Override
        public Fragment getItem(int position) {

            return createFragment(position);
        }

        @Override
        public int getCount() {
            return num;
        }

        private Fragment createFragment(int pos) {
            Fragment fragment = mFragmentHashMap.get(pos);

            if (fragment == null) {
                switch (pos) {
                    case 0:
                        fragment = TestFragment.ins(0);
                        break;
                    case 1:
                        fragment = TestFragment.ins(1);
                        break;
                    case 2:
                        fragment = TestFragment.ins(2);
                        break;
                    case 3:
                        fragment = TestFragment.ins(3);
                        break;
                }
                mFragmentHashMap.put(pos, fragment);
            }
            return fragment;
        }
    }
}
