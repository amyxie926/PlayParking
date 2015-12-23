package me.wztc.fragment.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;

/**
 * 关于我们页面
 */
public class AboutUsFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        view.findViewById(R.id.about_us_fragment_products_layout).setOnClickListener(this);
        view.findViewById(R.id.about_us_fragment_service_terms_layout).setOnClickListener(this);
        view.findViewById(R.id.about_us_fragment_use_info_layout).setOnClickListener(this);
        view.findViewById(R.id.about_us_fragment_user_feedback_layout).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.about_us_fragment_title);
    }
    



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.about_us_fragment_products_layout:// 产品介绍
            break;
        case R.id.about_us_fragment_service_terms_layout://服务条款
            break;
        case R.id.about_us_fragment_use_info_layout://使用说明

            break;
        case R.id.about_us_fragment_user_feedback_layout:// 用户反馈
            break;
        default:
            break;
        }
    }




    @Override
    public void onResume() {
        super.onResume();
    }
}
