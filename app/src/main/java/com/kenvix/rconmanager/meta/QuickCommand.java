package com.kenvix.rconmanager.meta;

import android.os.Parcel;
import android.os.Parcelable;

public class QuickCommand implements Cloneable, Parcelable {
    //private static final long serialVersionUID = 0xFA00002L;

    private int cid = -1;
    private String value;
    private String name;


    public QuickCommand(String name, String value) {
        this.value = value;
        this.name = name;
    }

    protected QuickCommand(Parcel in) {
        cid = in.readInt();
        value = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cid);
        dest.writeString(value);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuickCommand> CREATOR = new Creator<QuickCommand>() {
        @Override
        public QuickCommand createFromParcel(Parcel in) {
            return new QuickCommand(in);
        }

        @Override
        public QuickCommand[] newArray(int size) {
            return new QuickCommand[size];
        }
    };

    public int getCid() {
        return cid;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public QuickCommand clone() throws CloneNotSupportedException {
        return (QuickCommand) super.clone();
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }
}
