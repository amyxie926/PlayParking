package me.wztc.model.radio;

import com.wztc.R;

public enum RechargeType {
    recharge_50(R.string.recharge_fragment_recharge_50, R.string.recharge_fragment_recharge_give_50), recharge_100(
            R.string.recharge_fragment_recharge_100, R.string.recharge_fragment_recharge_give_100), recharge_200(
            R.string.recharge_fragment_recharge_200, R.string.recharge_fragment_recharge_give_300), recharge_500(
            R.string.recharge_fragment_recharge_500, R.string.recharge_fragment_recharge_give_700);

    private int rechargeMoney;
    private int giveMoeny;

    private RechargeType(int rechargeMoney, int giveMoeny) {
        this.rechargeMoney = rechargeMoney;
        this.giveMoeny = giveMoeny;
    }

    public int getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(int rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public int getGiveMoeny() {
        return giveMoeny;
    }

    public void setGiveMoeny(int giveMoeny) {
        this.giveMoeny = giveMoeny;
    }

}
