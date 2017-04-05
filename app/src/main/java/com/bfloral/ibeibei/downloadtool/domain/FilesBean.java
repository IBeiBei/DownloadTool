package com.bfloral.ibeibei.downloadtool.domain;

import android.os.Parcel;

/**
 * Created by MingLi on 10/19/16.
 */

public class FilesBean extends Model {
    private String fileId;
    private String fileName;
    private String mimeType;
    private String url;

    public FilesBean(){}

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    private FilesBean(Parcel in){
        fileId = in.readString();
        fileName = in.readString();
        mimeType = in.readString();
        url = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileId);
        dest.writeString(fileName);
        dest.writeString(mimeType);
        dest.writeString(url);

    }
    public static final Creator<FilesBean> CREATOR = new Creator<FilesBean>() {
        @Override
        public FilesBean createFromParcel(Parcel source) {
            return new FilesBean(source);
        }

        @Override
        public FilesBean[] newArray(int size) {
            return new FilesBean[size];
        }
    };
}
