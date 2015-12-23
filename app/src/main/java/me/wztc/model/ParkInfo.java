package me.wztc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 停车场信息
 */
public class ParkInfo implements Parcelable{
    private String parkId;//停车场编号
    private String parkName;//停车场名字
    private String address;//停车场地址
    private int capacity;//停车场车位数
    private String type;//停车场类型
    private double lng;//停车场经度
    private double lat;//停车场纬度
    private String remark;//备注
    private float price;//停车费单价

    public String getParkId() {
        return parkId;
    }

    public void setParkId(String parkId) {
        this.parkId = parkId;
    }

    public String getParkName() {
        return parkName;
    }

    public void setParkName(String parkName) {
        this.parkName = parkName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.parkId);
        dest.writeString(this.parkName);
        dest.writeString(this.address);
        dest.writeInt(this.capacity);
        dest.writeString(this.type);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeString(this.remark);
        dest.writeFloat(this.price);
    }

    public ParkInfo() {
    }

    protected ParkInfo(Parcel in) {
        this.parkId = in.readString();
        this.parkName = in.readString();
        this.address = in.readString();
        this.capacity = in.readInt();
        this.type = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.remark = in.readString();
        this.price = in.readFloat();
    }

    public static final Creator<ParkInfo> CREATOR = new Creator<ParkInfo>() {
        public ParkInfo createFromParcel(Parcel source) {
            return new ParkInfo(source);
        }

        public ParkInfo[] newArray(int size) {
            return new ParkInfo[size];
        }
    };
}
