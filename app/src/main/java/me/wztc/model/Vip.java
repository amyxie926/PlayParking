package me.wztc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会员卡
 *
 * @author amy_xie
 */
public class Vip implements Parcelable {
    private int vipId;
    private String marketName;// 商场名称
    private String vipType;// 会员卡类型
    private String carNumber;// 车牌号
    private long vipValidDate;// 有效时间

    public int getVipId() {
        return vipId;
    }

    public void setVipId(int vipId) {
        this.vipId = vipId;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public long getVipValidDate() {
        return vipValidDate;
    }

    public void setVipValidDate(long vipValidDate) {
        this.vipValidDate = vipValidDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.vipId);
        dest.writeString(this.marketName);
        dest.writeString(this.vipType);
        dest.writeString(this.carNumber);
        dest.writeLong(this.vipValidDate);
    }

    public Vip() {
    }

    protected Vip(Parcel in) {
        this.vipId = in.readInt();
        this.marketName = in.readString();
        this.vipType = in.readString();
        this.carNumber = in.readString();
        this.vipValidDate = in.readLong();
    }

    public static final Creator<Vip> CREATOR = new Creator<Vip>() {
        public Vip createFromParcel(Parcel source) {
            return new Vip(source);
        }

        public Vip[] newArray(int size) {
            return new Vip[size];
        }
    };
}
