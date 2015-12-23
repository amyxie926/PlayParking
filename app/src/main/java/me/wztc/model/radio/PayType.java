package me.wztc.model.radio;

import com.wztc.R;

public enum PayType {

    balancePay(R.drawable.icon_playparking_pay, R.string.recharge_fragment_playparking), alipay(
            R.drawable.icon_pay_alipay, R.string.pay_fragment_aliPay), weixin_pay(R.drawable.icon_pay_wechat,
            R.string.pay_fragment_weixin_pay);

    private int resId;
    private int nameId;

    private PayType(int resId, int nameId) {
        this.resId = resId;
        this.nameId = nameId;
    }

    public int getResId() {
        return resId;
    }

    public int getNameId() {
        return nameId;
    }

}
