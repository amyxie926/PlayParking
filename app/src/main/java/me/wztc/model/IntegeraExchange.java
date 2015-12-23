package me.wztc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/8/22.
 */
public class IntegeraExchange implements Parcelable {
    private String name;
    private String date;
    private String info;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.date);
        dest.writeString(this.info);
    }

    public IntegeraExchange() {
    }

    protected IntegeraExchange(Parcel in) {
        this.name = in.readString();
        this.date = in.readString();
        this.info = in.readString();
    }

    public static final Creator<IntegeraExchange> CREATOR = new Creator<IntegeraExchange>() {
        public IntegeraExchange createFromParcel(Parcel source) {
            return new IntegeraExchange(source);
        }

        public IntegeraExchange[] newArray(int size) {
            return new IntegeraExchange[size];
        }
    };
}
