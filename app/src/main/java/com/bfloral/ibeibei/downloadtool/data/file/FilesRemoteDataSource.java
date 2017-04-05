package com.bfloral.ibeibei.downloadtool.data.file;

import android.content.Context;
import android.text.TextUtils;

import com.bfloral.ibeibei.downloadtool.data.api.ApiService;
import com.bfloral.ibeibei.downloadtool.data.api.BackgroundCallback;
import com.bfloral.ibeibei.downloadtool.data.api.BaseUrl;
import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.domain.ResultError;
import com.bfloral.ibeibei.downloadtool.util.cache.DiskCacheUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by MingLi on 10/21/16.
 */
public class FilesRemoteDataSource implements FilesDataSource {
    private static FilesRemoteDataSource INSTANCE = null;
    private static ApiService service;

    private FilesRemoteDataSource() {
    }

    public static FilesRemoteDataSource getInstance() {
        if (INSTANCE == null || service == null) {
            INSTANCE = new FilesRemoteDataSource();
            service = new Retrofit.Builder()
                    .baseUrl(BaseUrl.url)
                    .client(new OkHttpClient.Builder()
                            .readTimeout(BaseUrl.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                            .writeTimeout(BaseUrl.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                            .connectTimeout(BaseUrl.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                            .followRedirects(false)
                            .followSslRedirects(false)
                            .build())
                    .build()
                    .create(ApiService.class);
        }
        return INSTANCE;
    }

    @Override
    public void getFile(final Context context, final FilesBean filesBean, final GetFilesCallback callback) {
        if (!TextUtils.isEmpty(filesBean.getUrl()) && !filesBean.getUrl().equals("null")) {
            downLoadFileWithUrl(context,filesBean, callback);
            return;
        }
        checkNotNull(filesBean.getFileId());
        checkNotNull(filesBean.getMimeType());
        String mimeType = filesBean.getMimeType();
        String startType = mimeType.split("/")[0];
        Call<ResponseBody> call;

        switch (startType) {
            case "image":
                call = service.getImage("{token}", "{param1}", filesBean.getFileId());
                break;
            default:
                call = service.getAttachment("{token}", "{param1}", filesBean.getFileId());
                break;
        }
        call.enqueue(new BackgroundCallback<ResponseBody>() {
            @Override
            public boolean onResultOk(int code, Headers headers, ResponseBody result) {
                try {
                    DiskCacheUtil.getDiskCacheInstance(context).put(filesBean.getFileId(), result.byteStream());
                    callback.onFileLoaded(DiskCacheUtil.getFile(context, filesBean.getFileId()));
                } catch (IOException e) {
                    callback.onDataNotAvailable(e.getMessage());
                }
                return false;
            }

            @Override
            public boolean onResultError(int code, Headers headers, ResultError error) {
                callback.onDataNotAvailable(error.getError().getMessage());
                return false;
            }

            @Override
            public boolean onCallException(Throwable t, ResultError error) {
                callback.onDataNotAvailable(error.getError().getMessage());
                return false;
            }
        });


    }

    private void downLoadFileWithUrl(final Context context,final FilesBean filesBean, final GetFilesCallback callback) {
        ApiService service = new Retrofit.Builder()
                .baseUrl(BaseUrl.url)
                .client(new OkHttpClient.Builder()
                        .readTimeout(BaseUrl.READ_TIME_OUT, TimeUnit.MILLISECONDS)
                        .writeTimeout(BaseUrl.WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                        .connectTimeout(BaseUrl.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                        .followRedirects(false)
                        .followSslRedirects(false)
                        .build())
                .build()
                .create(ApiService.class);
        Call<ResponseBody> call = service.downLoadFileWithDynamicUrl(filesBean.getUrl());
        call.enqueue(new BackgroundCallback<ResponseBody>() {
            @Override
            public boolean onResultOk(int code, Headers headers, ResponseBody result) {
                try {
                    DiskCacheUtil.getDiskCacheInstance(context).put(filesBean.getUrl(), result.byteStream());
                    callback.onFileLoaded(DiskCacheUtil.getFile(context, filesBean.getUrl()));
                } catch (IOException e) {
                    callback.onDataNotAvailable(e.getMessage());
                }
                callback.onFileLoaded(DiskCacheUtil.getFile(context, filesBean.getUrl()));
                return false;
            }

            @Override
            public boolean onResultError(int code, Headers headers, ResultError error) {
                callback.onDataNotAvailable(error.getError().getMessage());
                return false;
            }

            @Override
            public boolean onCallException(Throwable t, ResultError error) {
                callback.onDataNotAvailable(error.getError().getMessage());
                return false;
            }
        });
    }
}
