package me.wztc.model;

import android.os.Parcel;
import android.os.Parcelable;
import me.wztc.util.AppUtils;

public class Bill implements Parcelable {
    private String wztcOrder;
    private String userId;
    private String plateNo;
    private String billStatus;
    private String parkId;
    private String parkName;
    private String fee;
    private String parkBillNo;
    private String entryTime;
    private String delayTime;

    public String getWztcOrder() {
        return wztcOrder;
    }

    public void setWztcOrder(String wztcOrder) {
        this.wztcOrder = wztcOrder;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(String delayTime) {
        this.delayTime = delayTime;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getParkBillNo() {
        return parkBillNo;
    }

    public void setParkBillNo(String parkBillNo) {
        this.parkBillNo = parkBillNo;
    }

    public String getFee() {
        return fee;
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

    public void setFee(String fee) {
        this.fee = fee;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Bill() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.wztcOrder);
        dest.writeString(this.userId);
        dest.writeString(this.plateNo);
        dest.writeString(this.billStatus);
        dest.writeString(this.parkId);
        dest.writeString(this.parkName);
        dest.writeString(this.fee);
        dest.writeString(this.parkBillNo);
        dest.writeString(this.entryTime);
        dest.writeString(this.delayTime);
    }

    protected Bill(Parcel in) {
        this.wztcOrder = in.readString();
        this.userId = in.readString();
        this.plateNo = in.readString();
        this.billStatus = in.readString();
        this.parkId = in.readString();
        this.parkName = in.readString();
        this.fee = in.readString();
        this.parkBillNo = in.readString();
        this.entryTime = in.readString();
        this.delayTime = in.readString();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        public Bill createFromParcel(Parcel source) {
            return new Bill(source);
        }

        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };
}
