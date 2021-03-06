package me.wztc.fragment.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;
import com.alipay.sdk.app.PayTask;
import com.wztc.R;
import me.wztc.api.request.pay.PayInfoSyncRequest;
import me.wztc.api.response.APIResponseHandler;
import me.wztc.api.response.pay.PayInfoSyncResponse;
import me.wztc.fragment.BaseFragment;
import me.wztc.model.Bill;
import me.wztc.notification.Notification;
import me.wztc.notification.NotificationCenter;
import me.wztc.util.AppConstants;
import me.wztc.util.AppUtils;
import me.wztc.util.ToastUtil;
import me.wztc.util.alipay.PayResult;
import me.wztc.util.alipay.SignUtils;
import me.wztc.wxapi.PayActivity;
import com.tencent.mm.sdk.modelbase.BaseResp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class BasePayFragment extends BaseFragment {
    //商户PID
    public static final String PARTNER = "2088711990616848";
    //商户收款账号
    public static final String SELLER = "taowei@iparking.me";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAOSpsP53E3ij4m43LfqlNe9kOZhUn1UzbKE4gSiJIPc6/k2OG5xNfLlpxfLSjD0pSEAQXJUamF9oLaebr4lmOSKzvsd4LLf8/507DxdruOwr+goBjl7rVJBXzhB3B8292U6ZFjtftCOi/a3w0TK9X+cIXJKb3rsnstppfTLXEy+3AgMBAAECgYAClPfup6GMpy0TWYQnZF4ridIsqifyalY7q8upjYLVx9C/R2+AYE+cOFmH2GDFAAZU0tp4xIgb6604S0W8I29Zs38YPIMxkct8OxwZ26xWG34uorbikH0nippfpDySrcrQnODqOITlVtuloMTufIeIjJslJRBdVotfkZVmZD19wQJBAPOWjCIi6bYc8Fv2AmFqZ8rWrjkQFbHKF4pkbNz7jmFVHQpIKc91zrEALGLgYbFFPiWdF3ExP7j26hKx/0Ic04kCQQDwUHQ1BlFk9rsyAptYbFgoTYT8jRC0y3/OIkoi992tpJKHW4awYEiTjUBfY+2Z72Ol95xOIkXXp9W3UtImTNk/AkAew5O9N/WWZ38/zgks7nhfWM+2Kz7iLmjctKQ/IJIx3sMFKmoFZpaNKj1w22/bKKiZrYZpaFPo815KsCpR2jwpAkAD3HcRhwl1wtUdJ5eRYyRDeYNif22+SerCyCBC0ZK5QISuMhVG7jUcUc+v+K4PpB4Iw6K6Sqiun166tFT2EpQHAkB6YhOdStlat8/pqq/lVdOKgDiCA8wI03Uvd7sMHqyPLq+OFq2oLkj5N1cuAqB7QLzg2OxTMWPHLpWHymUvGBx7";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";


    private static final int SDK_PAY_FLAG = 1;

    protected Bill bill;

    private String orderNo;

    public void onPaySuccess(String orderNo) {
        if (bill != null)
            syncPayResult(bill.getWztcOrder(), bill.getParkBillNo(), bill.getFee(), "0");
    }

    public void onPayFailed() {

    }

    /**
     * 调起微信支付
     * @param priceFeng 单位为分
     */
    public void weixinPay(String priceFeng) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.BUNDLE_PRICE_FENG, priceFeng);
        intent.setClass(getActivity(), PayActivity.class);
        startActivity(intent);
    }

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void aliPay(String subject, String body, String price) {
        // 订单
        String orderInfo = getOrderInfo(subject, body, price);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        showLoadingDialog();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        orderNo = key;
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    protected void syncPayResult(String wztcOrder, String orderNo, String amount, String discount) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.BUNDLE_LOADING_DIALOG_MESSAGE, getString(R.string.pay_fragment_bill_sync));
        showLoadingDialog(bundle);
        PayInfoSyncRequest request = new PayInfoSyncRequest(wztcOrder, orderNo, amount, discount);
        request.start(new APIResponseHandler<PayInfoSyncResponse>() {
            @Override
            public void handleError(String errorCode, String errorMessage) {
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }
                if (AppUtils.isEmpty(errorMessage)) {
                    errorMessage = getString(R.string.pay_fragment_bill_sync_failed);
                }
                ToastUtil.toastLong(getActivity(), errorMessage);
            }

            @Override
            public void handleResponse(PayInfoSyncResponse response) {
                dismissLoadingDialog();
                if (getActivity() == null) {
                    return;
                }

                ToastUtil.toastLong(getActivity(), R.string.pay_fragment_bill_sync_success);
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_GO_BACK));
                //进入支付结果画面
                PayResultFragment fragment = new PayResultFragment();
                Bundle data = new Bundle();
                data.putInt(PayFragment.PAY_RESULT_STATUS, PayFragment.PAY_RESULT_STATUS_1);
                fragment.setArguments(data);
                mCenter.postNotification(new Notification(AppConstants.NOTIFICATION_ADD_FRAGMENT, fragment));
            }
        });
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            dismissLoadingDialog();
            switch (msg.what) {
            case SDK_PAY_FLAG: {
                PayResult payResult = new PayResult((String) msg.obj);

                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                String resultInfo = payResult.getResult();

                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Toast.makeText(getActivity(), R.string.pay_fragment_pay_success,
                            Toast.LENGTH_SHORT).show();
                    onPaySuccess(orderNo);
                } else {
                    onPayFailed();
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        Toast.makeText(getActivity(), R.string.pay_fragment_paying,
                                Toast.LENGTH_SHORT).show();

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        Toast.makeText(getActivity(), R.string.pay_fragment_pay_failed,
                                Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            }
            default:
                break;
            }
        }

    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_WEI_XIN, weiXinListener);
        mCenter.removeNotificationListener(AppConstants.NOTIFICATION_WEI_XIN_ORDER_NO, weiXinOrderNoListener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_WEI_XIN, weiXinListener);
        mCenter.addNotificationListener(AppConstants.NOTIFICATION_WEI_XIN_ORDER_NO, weiXinOrderNoListener);
    }

    private NotificationCenter.NotificationListener weiXinListener = new NotificationCenter.NotificationListener() {
        @Override
        public void notificationReceived(Notification notification) {
            int code = (int) notification.getObject();
            if (code == BaseResp.ErrCode.ERR_OK) {
                //支付成功
                Toast.makeText(getActivity(), R.string.pay_fragment_pay_success, Toast.LENGTH_SHORT).show();
                onPaySuccess(orderNo);
            } else {
                //支付失败
                Toast.makeText(getActivity(), R.string.pay_fragment_pay_failed, Toast.LENGTH_SHORT).show();
                onPayFailed();
            }
        }
    };

    private NotificationCenter.NotificationListener weiXinOrderNoListener = new NotificationCenter.NotificationListener() {
        @Override
        public void notificationReceived(Notification notification) {
            orderNo = (String) notification.getObject();
        }
    };
}
