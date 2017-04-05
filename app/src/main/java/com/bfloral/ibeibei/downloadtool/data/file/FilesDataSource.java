package com.bfloral.ibeibei.downloadtool.data.file;


import android.content.Context;

import com.bfloral.ibeibei.downloadtool.domain.FilesBean;

import java.io.File;

/**
 * Created by MingLi on 10/21/16.
 */

public interface FilesDataSource {
    interface GetFilesCallback{
        void onFileLoaded(File file);
        void onDataNotAvailable(String errorMessage);
    }
    void getFile(Context context,FilesBean filesBean, GetFilesCallback callback);
}
