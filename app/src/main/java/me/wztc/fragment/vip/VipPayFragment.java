package me.wztc.fragment.vip;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.fragment.carmanager.CarManagerFragment;
import me.wztc.model.radio.PayType;
import me.wztc.notification.Notification;
import me.wztc.ui.RadioView;
import me.wztc.util.AppConstants;

/**
 * 会员卡支付画面
 */
public class VipPayFragment extends BaseFragment {
    public static final String VIP_PAY_RESULT_STATUS = "recharge_result_status";
    public static final int VIP_PAY_RESULT_STATUS_0 = 0;// 失败
    public static final int VIP_PAY_RESULT_STATUS_1 = 1;// 成功

    private RadioView payTypeRb;// 支付选择

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip_pay, container, false);

        payTypeRb = (RadioView) view.findViewById(R.id.vip_pay_fragment_pay_type);
        ArrayList<Object> payTypes = new ArrayList<Object>();
        payTypes.add(PayType.balancePay);
        payTypes.add(PayType.alipay);
        payTypes.add(PayType.weixin_pay);

        payTypeRb.setLeftView(payTypes);
        payTypeRb.setSelectedRadio(0);

        view.findViewById(R.id.vip_pay_fragment_addcar).setOnClickListener(this);
        view.findViewById(R.id.vip_pay_fragment_confrim_button).setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.recharge_fragment_title);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.vip_pay_fragment_addcar://添加车辆
                mCenter.postNotification(
                        new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new CarManagerFragment()));
                break;
        case R.id.vip_pay_fragment_confrim_button:// 确认支付
            BaseFragment resultFragment = new VipPayResultFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(VIP_PAY_RESULT_STATUS, VIP_PAY_RESULT_STATUS_1);
            resultFragment.setArguments(bundle);
            mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, resultFragment));
            break;
        default:
            break;
        }
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
