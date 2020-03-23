package com.hx.wjbaseproject.ui.test;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hx.wjbaseproject.R;
import com.hx.wjbaseproject.ui.base.BaseFragment;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: 王杰
 * @描述:
 * @文件名: TestFragment
 * @包名: com.hx.wjbaseproject.ui.test
 * @创建时间: 2020-03-23 17:10
 * @修改人: 王杰
 * @公司: 北京和信金谷科技有限公司
 * @备注:
 * @版本号: 1.0.0
 */
public class TestFragment extends BaseFragment {

    private static final String KEY_PAGE_INDEX = "PAGE_INDEX";
    @BindView(R.id.test_text)
    TextView testText;

    Unbinder unbinder;

    public static TestFragment ins(int index) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_PAGE_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        int index = getArguments().getInt(KEY_PAGE_INDEX, 0);
        testText.setText("页面" + index);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
