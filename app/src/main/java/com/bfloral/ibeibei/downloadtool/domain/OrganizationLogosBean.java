package com.bfloral.ibeibei.downloadtool.domain;

import android.os.Parcel;

/**
 * Created by MingLi on 10/19/16.
 */

public class OrganizationLogosBean extends Model {
    private String fileId;
    private String logoSize;
    private int width;
    private int height;
    private String url;

    public OrganizationLogosBean(){}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getLogoSize() {
        return logoSize;
    }

    public void setLogoSize(String logoSize) {
        this.logoSize = logoSize;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    private OrganizationLogosBean(Parcel in){
        fileId = in.readString();
        logoSize = in.readString();
        width = in.readInt();
        height = in.readInt();
        url = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileId);
        dest.writeString(logoSize);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(url);
    }
    public static final Creator<OrganizationLogosBean> CREATOR = new Creator<OrganizationLogosBean>() {
        @Override
        public OrganizationLogosBean createFromParcel(Parcel source) {
            return new OrganizationLogosBean(source);
        }

        @Override
        public OrganizationLogosBean[] newArray(int size) {
            return new OrganizationLogosBean[size];
        }
    };
}
