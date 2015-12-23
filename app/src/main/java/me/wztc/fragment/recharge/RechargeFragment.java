package me.wztc.fragment.recharge;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wztc.R;
import me.wztc.api.request.rechange.ReChangeRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.rechange.ReChangeResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.fragment.pay.BasePayFragment;
import me.wztc.model.User;
import me.wztc.model.radio.PayType;
import me.wztc.model.radio.RechargeType;
import me.wztc.notification.Notification;
import me.wztc.ui.RadioView;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;
import me.wztc.util.UserKeeper;

/**
 * 充值
 */
public class RechargeFragment extends BasePayFragment {
    public static final String RECHARGE_RESULT_STATUS = "recharge_result_status";
    public static final int RECHARGE_RESULT_STATUS_0 = 0;// 失败
    public static final int RECHARGE_RESULT_STATUS_1 = 1;// 成功

    private RadioView rechargeTypeRb;// 充值选择

    private RadioView payTypeRb;// 支付选择

    private String fee;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recharge, container, false);

        rechargeTypeRb = (RadioView) view.findViewById(R.id.recharge_fragment_recharge_type);
        ArrayList<Object> rechargeTypes = new ArrayList<Object>();
        rechargeTypes.add(RechargeType.recharge_50);
        rechargeTypes.add(RechargeType.recharge_100);
        rechargeTypes.add(RechargeType.recharge_200);
        rechargeTypes.add(RechargeType.recharge_500);
        rechargeTypeRb.setLeftView(rechargeTypes);
        rechargeTypeRb.setSelectedRadio(0);

        payTypeRb = (RadioView) view.findViewById(R.id.recharge_fragment_pay_type);
        ArrayList<Object> payTypes = new ArrayList<Object>();
        payTypes.add(PayType.alipay);
        payTypes.add(PayType.weixin_pay);

        payTypeRb.setLeftView(payTypes);
        payTypeRb.setSelectedRadio(0);

        view.findViewById(R.id.recharge_fragment_confrim_button).setOnClickListener(this);
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
        case R.id.recharge_fragment_confrim_button:// 确认支付
            pay();
            break;
        default:
            break;
        }
    }

    private void pay() {
        switch (rechargeTypeRb.getSelectRadio()) {
        case 0://50
            fee = "5000";
            break;
        case 1://100
            fee = "10000";
            break;
        case 2://200
            fee = "20000";
            break;
        case 3://500
            fee = "50000";
            break;
        }

        switch (payTypeRb.getSelectRadio()) {
        case 0://alipay
            aliPay(getString(R.string.pay_fragment_product_name), getString(R.string.pay_fragment_product_description), getPriceStr());
            break;
        case 1://weixin
            weixinPay(fee);
            break;
        }
    }

    public String getPriceStr() {
        if (AppUtils.isEmpty(fee)) {
            return "0.00";
        }

        String feeTemp = new String(fee);
        int length = feeTemp.length();
        if (length < 3) {
            for (int i = length; i < 3; i++) {
                feeTemp = "0" + feeTemp;
            }
        }
        StringBuilder str = new StringBuilder();
        str.append(feeTemp.substring(0, feeTemp.length() - 2));
        str.append(".");
        str.append(feeTemp.substring(feeTemp.length() - 2));
        return str.toString();
    }

    @Override
    public void onPaySuccess(String orderNo) {
        User user = LocalDataBuffer.getInstance().getUser();
        if (user == null) {
            return;
        }
        showLoadingDialog();
        ReChangeRequest request = new ReChangeRequest(user.getUserId(), fee, orderNo);
        request.start(new APIResponseHandler<ReChangeResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                dismissLoadingDialog();
                ToastUtil.toastLong(getActivity(), errorMessage);
            }

            @Override
            public void handleResponse(ReChangeResponse response) {
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                User u = LocalDataBuffer.getInstance().getUser();
                if (u != null) {
                    u.setMoney(response.getMoney());
                    UserKeeper.saveUser(getActivity(), u);
                }
                BaseFragment resultFragment = new RechargeResultFragment();

                Bundle bundle = new Bundle();
                bundle.putInt(RECHARGE_RESULT_STATUS, RECHARGE_RESULT_STATUS_1);
                resultFragment.setArguments(bundle);
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, resultFragment));
            }
        });

    }

    @Override
    public void onPayFailed() {
        BaseFragment resultFragment = new RechargeResultFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(RECHARGE_RESULT_STATUS, RECHARGE_RESULT_STATUS_0);
        resultFragment.setArguments(bundle);
        mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, resultFragment));
    }
}
