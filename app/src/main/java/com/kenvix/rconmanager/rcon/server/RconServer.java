// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.server;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.kenvix.utils.StringTools;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class RconServer implements Cloneable, Parcelable {
    //private static final long serialVersionUID = 0xf00001L;

    private int sid = -1;
    private String host;
    private String password;
    private String name;
    private int port;

    public RconServer(String name, String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.name = name;
    }

    public RconServer() {
    }

    public RconServer(Parcel parcel) {
        name = parcel.readString();
        host = parcel.readString();
        password = parcel.readString();
        port = parcel.readInt();
        sid = parcel.readInt();
    }

    public static final Creator<RconServer> CREATOR = new Creator<RconServer>() {
        @Override
        public RconServer createFromParcel(Parcel in) {
            return new RconServer(in);
        }

        @Override
        public RconServer[] newArray(int size) {
            return new RconServer[size];
        }
    };

    public int getPort() {
        return port;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getHost() {
        return host;
    }

    public int getSid() {
        return sid;
    }

    public RconServer setSid(int sid) {
        this.sid = sid;
        return this;
    }

    public String getHostAndPort() {
        return host + ":" + port;
    }

    @Override
    public RconServer clone() throws CloneNotSupportedException {
       return (RconServer) super.clone();
    }

    public void setHost(@NonNull String host) {
        this.host = host;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setPort(int port) {
        this.port = port;
    }


    /**
     * override this method with CONTENTS_FILE_DESCRIPTOR if sub class has file descriptor
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(host);
        dest.writeString(password);
        dest.writeInt(port);
        dest.writeInt(sid);
    }

    public String getRconURLString() throws UnsupportedEncodingException {
        return StringTools.format("rcon://%s@%s:%d#%s", URLEncoder.encode(password, "utf-8"), host, port, URLEncoder.encode(name, "utf-8"));
    }

    public URL getRconURL() throws UnsupportedEncodingException, MalformedURLException {
        return new URL(getRconURLString());
    }
}
