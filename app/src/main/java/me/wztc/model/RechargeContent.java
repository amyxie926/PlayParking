package me.wztc.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RechargeContent implements Parcelable {
    private int id;
    private String rechargeMoeny;
    private String giveMoeny;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRechargeMoeny() {
        return rechargeMoeny;
    }

    public void setRechargeMoeny(String rechargeMoeny) {
        this.rechargeMoeny = rechargeMoeny;
    }

    public String getGiveMoeny() {
        return giveMoeny;
    }

    public void setGiveMoeny(String giveMoeny) {
        this.giveMoeny = giveMoeny;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.rechargeMoeny);
        dest.writeString(this.giveMoeny);
    }

    public RechargeContent() {
    }

    protected RechargeContent(Parcel in) {
        this.id = in.readInt();
        this.rechargeMoeny = in.readString();
        this.giveMoeny = in.readString();
    }

    public static final Creator<RechargeContent> CREATOR = new Creator<RechargeContent>() {
        public RechargeContent createFromParcel(Parcel source) {
            return new RechargeContent(source);
        }

        public RechargeContent[] newArray(int size) {
            return new RechargeContent[size];
        }
    };
}
