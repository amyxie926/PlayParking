package me.wztc.fragment.vip;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.Vip;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

/**
 * 会员卡搜索画面
 */
public class VipDetailslFragment extends BaseFragment {
    private LinearLayout vipInfoLayout;
    private TextView marketnameTv;
    private TextView vipTypeTv;
    private TextView carNumberTv;
    private TextView vipValidDateTv;
    private TextView infoTv;
    private TextView useInfoTv;
    private LinearLayout btnLayout;
    private TextView buyTv;
    private TextView residualAmountTv;

    private Vip vip;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip_details, container, false);
        initView(view);
        setData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            vip = getArguments().getParcelable(AppConstants.BUNDLE_VIP);
            setTitle(vip.getMarketName());
        }


    }

    private void initView(View view) {
        vipInfoLayout = (LinearLayout) view.findViewById(R.id.vip_details_fragment_vip_info_layout);
        marketnameTv = (TextView) view.findViewById(R.id.vip_details_fragment_marketname_tv);
        vipTypeTv = (TextView) view.findViewById(R.id.vip_details_fragment_vip_type_tv);
        carNumberTv = (TextView) view.findViewById(R.id.vip_details_fragment_car_number_tv);
        vipValidDateTv = (TextView) view.findViewById(R.id.vip_details_fragment_vip_valid_date_tv);
        infoTv = (TextView) view.findViewById(R.id.vip_details_fragment_info_tv);
        useInfoTv = (TextView) view.findViewById(R.id.vip_details_fragment_use_info_tv);
        btnLayout = (LinearLayout) view.findViewById(R.id.vip_details_fragment_button);
        buyTv = (TextView) view.findViewById(R.id.vip_details_fragment_buy_tv);
        residualAmountTv = (TextView) view.findViewById(R.id.vip_details_fragment_residual_amount_tv);

        btnLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.vip_details_fragment_button:
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new VipPayFragment()));
                break;

            default:
                break;
        }
    }

    private void setData() {
        String residualAmount = String.format(getString(R.string.vip_details_fragment_residual_amount), 10);
        residualAmountTv.setText(residualAmount);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
