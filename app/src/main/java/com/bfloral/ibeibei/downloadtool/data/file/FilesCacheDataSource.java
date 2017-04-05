package com.bfloral.ibeibei.downloadtool.data.file;

import android.content.Context;
import android.text.TextUtils;

import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.util.cache.DiskCacheUtil;
import com.bfloral.ibeibei.downloadtool.util.cache.SimpleDiskCache;

import java.io.File;

/**
 * Created by MingLi on 10/21/16.
 */
public class FilesCacheDataSource implements FilesDataSource {
    private static FilesCacheDataSource INSTANCE=null;

    private FilesCacheDataSource(){}

    public static FilesCacheDataSource getInstance(){
        if (INSTANCE ==null){
            INSTANCE = new FilesCacheDataSource();
        }
        return INSTANCE;
    }

    @Override
    public void getFile(Context context,FilesBean filesBean, GetFilesCallback callback) {
        String cacheId;
        if (TextUtils.isEmpty(filesBean.getUrl()) || filesBean.getUrl().equals("null")){
            cacheId = filesBean.getFileId();
            if (TextUtils.isEmpty(cacheId) || cacheId.equals("null")){
                return;
            }
        }else{
            cacheId=filesBean.getUrl();
        }
        SimpleDiskCache simpleDiskCache = DiskCacheUtil.getDiskCacheInstance(context);
        if(DiskCacheUtil.isExist(simpleDiskCache,cacheId)){
            File file =DiskCacheUtil.getFile(context,cacheId);
            if (file!=null){
                callback.onFileLoaded(file);
            }else{
                callback.onDataNotAvailable(null);
            }
        }else{
            callback.onDataNotAvailable(null);
        }

    }
}
