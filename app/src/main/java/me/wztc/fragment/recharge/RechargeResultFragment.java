package me.wztc.fragment.recharge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.fragment.BaseFragment;
import me.wztc.notification.Notification;
import me.wztc.util.AppConstants;

/**
 * 充值结果显示
 */
public class RechargeResultFragment extends BaseFragment {
    private TextView resultStatusTv;
    private Button resultBtn;

    private int resultStatus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recharge_result, container, false);

        initView(view);
        getData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.recharge_result_fragment_title);
    }

    private void initView(View view) {
        resultStatusTv = (TextView) view.findViewById(R.id.recharge_fragment_result_status);
        resultBtn = (Button) view.findViewById(R.id.recharge_result_fragment_button);
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (getArguments() != null) {
            resultStatus = getArguments().getInt(RechargeFragment.RECHARGE_RESULT_STATUS);

            if (resultStatus == RechargeFragment.RECHARGE_RESULT_STATUS_0) {// 失败
                resultStatusTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_pay_fail, 0, 0, 0);
                resultStatusTv.setText(R.string.pay_result_fragment_fail);
                resultBtn.setText(R.string.pay_result_fragment_fail_btn);
            } else {// 成功
                resultStatusTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_pay_success, 0, 0, 0);
                resultStatusTv.setText(R.string.pay_result_fragment_success);
                resultBtn.setText(R.string.base_ok);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.pay_result_fragment_button:
            if (resultStatus == RechargeFragment.RECHARGE_RESULT_STATUS_0) {// 失败
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT,
                        new RechargeFragment()));
            } else {

            }
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
