package com.bfloral.ibeibei.downloadtool.data.file;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bfloral.ibeibei.downloadtool.domain.FilesBean;

import java.io.File;

/**
 * Created by MingLi on 10/21/16.
 */

public class FilesRepository implements FilesDataSource {
    private static FilesRepository INSTANCE = null;
    private static FilesDataSource mCacheFilesDataSource;
    private static FilesDataSource mRemoteFilesDataSource;

    private FilesRepository(@NonNull FilesDataSource mCacheFilesDataSource, @NonNull FilesDataSource mRemoteFilesDataSource) {
        this.mCacheFilesDataSource = mCacheFilesDataSource;
        this.mRemoteFilesDataSource = mRemoteFilesDataSource;
    }

    public static FilesRepository getInstance(@NonNull FilesDataSource mCacheFilesDataSource, @NonNull FilesDataSource mRemoteFilesDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new FilesRepository(mCacheFilesDataSource, mRemoteFilesDataSource);
        }
        return INSTANCE;
    }

    @Override
    public void getFile(final Context context, final FilesBean filesBean, final GetFilesCallback callback) {
        mCacheFilesDataSource.getFile(context,filesBean, new GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                callback.onFileLoaded(file);
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                mRemoteFilesDataSource.getFile(context,filesBean, new GetFilesCallback() {
                    @Override
                    public void onFileLoaded(File file) {
                        callback.onFileLoaded(file);
                    }

                    @Override
                    public void onDataNotAvailable(String errorMessage) {
                        callback.onDataNotAvailable(errorMessage);
                    }
                });
            }
        });
    }
}
