package me.wztc.fragment.pay;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wztc.R;
import me.wztc.api.request.pay.PayBillRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.pay.PayBillResponse;
import me.wztc.fragment.carmanager.CarManagerFragment;
import me.wztc.model.Bill;
import me.wztc.model.User;
import me.wztc.model.radio.PayType;
import me.wztc.notification.Notification;
import me.wztc.ui.RadioView;
import me.wztc.util.AppConstants;
import me.wztc.util.LocalDataBuffer;
import me.wztc.util.ToastUtil;

/**
 * 支付
 */
public class PayFragment extends BasePayFragment {
    public static final String PAY_RESULT_STATUS = "pay_result_status";
    public static final int PAY_RESULT_STATUS_0 = 0;// 失败
    public static final int PAY_RESULT_STATUS_1 = 1;// 成功

    private TextView plateNumberTv;// 车牌号码
    private TextView intoStorehouseTimeTv;// 进库时间
    private TextView outStorehouseTimeTv;// 出库时间
    private TextView invitedFeesTv;// 应缴费用
    private TextView subsidyTv;// 补贴
    private TextView couponTv;// 优惠券
    private TextView paidUpFeeTv;// 实缴费用
    private RadioView payTypeRb;// 支付选择


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        plateNumberTv = (TextView) view.findViewById(R.id.pay_fragment_plate_number_tv);
        intoStorehouseTimeTv = (TextView) view.findViewById(R.id.pay_fragment_into_storehouse_time_tv);
        outStorehouseTimeTv = (TextView) view.findViewById(R.id.pay_fragment_out_storehouse_time_tv);
        invitedFeesTv = (TextView) view.findViewById(R.id.pay_fragment_invited_fees_tv);
        subsidyTv = (TextView) view.findViewById(R.id.pay_fragment_subsidy_tv);
        couponTv = (TextView) view.findViewById(R.id.pay_fragment_coupon_tv);
        paidUpFeeTv = (TextView) view.findViewById(R.id.pay_fragment_paid_up_fee_tv);

        payTypeRb = (RadioView) view.findViewById(R.id.pay_fragment_rb);

        ArrayList<Object> payTypes = new ArrayList<Object>();
        payTypes.add(PayType.balancePay);
        payTypes.add(PayType.alipay);
        payTypes.add(PayType.weixin_pay);

        payTypeRb.setLeftView(payTypes);
        payTypeRb.setSelectedRadio(0);

        view.findViewById(R.id.pay_fragment_confrim_button).setOnClickListener(this);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.pay_fragment_title);
        if (!isInit) {
            getPayInfo();
            isInit = true;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pay_fragment_confrim_button:// 确认支付
                pay();
                break;
            default:
                break;
        }
    }

    private void getPayInfo() {

        User user = LocalDataBuffer.getInstance().getUser();
        if (user == null) {
            return;
        }

        showLoadingDialog();
        PayBillRequest request = new PayBillRequest(user.getUserId());
        request.start(new APIResponseHandler<PayBillResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                if (errorCode.equals("1")) {
                    //1-未绑定车牌，跳到车牌绑定页面
                    mCenter.postNotification(
                            new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, new CarManagerFragment()));
                } else if (errorCode.equals("2")) {
                    //2-未找到停车信息，提示用户
                    ToastUtil.toastShort(getActivity(), errorMessage);
                } else {
                    ToastUtil.toastShort(getActivity(), errorMessage);
                }
            }

            @Override
            public void handleResponse(PayBillResponse response) {
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                bill = response.getBill();
                setPayInfo(bill);
            }
        });

    }

    private void setPayInfo(Bill bill) {

        plateNumberTv.setText(bill.getPlateNo());
        intoStorehouseTimeTv.setText(bill.getEntryTime());
        outStorehouseTimeTv.setText(bill.getDelayTime());
        invitedFeesTv.setText(bill.getPriceStr() + getString(R.string.pay_fragment_yuan));

    }

    private void pay() {
        int index = payTypeRb.getSelectRadio();

        if (index == 0) {//玩转支付

        } else if (index == 1) {//支付宝支付
            aliPay(getString(R.string.pay_fragment_product_name), getString(R.string.pay_fragment_product_description), bill.getPriceStr());
        } else if (index == 2) {//微信支付
            weixinPay(bill.getFee());
        }
    }
}
