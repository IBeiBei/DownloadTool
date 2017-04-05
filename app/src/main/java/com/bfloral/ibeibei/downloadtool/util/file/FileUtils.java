package com.bfloral.ibeibei.downloadtool.util.file;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import com.bfloral.ibeibei.downloadtool.data.file.FilesCacheDataSource;
import com.bfloral.ibeibei.downloadtool.data.file.FilesDataSource;
import com.bfloral.ibeibei.downloadtool.data.file.FilesRemoteDataSource;
import com.bfloral.ibeibei.downloadtool.data.file.FilesRepository;
import com.bfloral.ibeibei.downloadtool.domain.FilesBean;
import com.bfloral.ibeibei.downloadtool.domain.OrganizationLogosBean;

import java.io.File;
import java.util.List;

/**
 * Created by MingLi on 10/22/16.
 */

public class FileUtils {
    private static FilesRepository filesRepository;

    private static FilesRepository getFilesRepository() {
        return filesRepository = FilesRepository.getInstance(FilesCacheDataSource.getInstance(), FilesRemoteDataSource.getInstance());
    }

    public static String getMIMEType(String fName) {

        String type = "*/*";
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end.equals("")) return type;
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    public static void loadImage(final Context context, final int reqWidth, FilesBean filesBean, final ImageCallback callback) {
        getFilesRepository().getFile(context,filesBean, new FilesDataSource.GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                options.inSampleSize = getScaleSampleSizeForWidth(reqWidth,options);
                options.inJustDecodeBounds = false;
                ThreadPool.execute(new DecodeBitmapTask(file,options,callback,new Handler(Looper.getMainLooper())));
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                callback.imageLoadedFail(errorMessage);

            }
        });
    }

    public static void loadImage(final Context context,final int reqWidth, final int reqHeight, FilesBean filesBean, final ImageCallback callback) {
        getFilesRepository().getFile(context,filesBean, new FilesDataSource.GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                options.inSampleSize = getFitInSampleSize(reqWidth, reqHeight, options);
                options.inJustDecodeBounds = false;
                ThreadPool.execute(new DecodeBitmapTask(file,options,callback,new Handler(Looper.getMainLooper())));
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                callback.imageLoadedFail(errorMessage);

            }
        });
    }

    public static void loadImage(final Context context,final ImageView imageView, FilesBean filesBean, final ImageCallback callback) {
        getFilesRepository().getFile(context,filesBean, new FilesDataSource.GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                imageView.measure(w, h);
                int height = imageView.getMeasuredHeight();
                int width = imageView.getMeasuredWidth();
                options.inSampleSize = getFitInSampleSize(width, height, options);
                options.inJustDecodeBounds = false;
                ThreadPool.execute(new DecodeBitmapTask(file,options,callback,new Handler(Looper.getMainLooper())));
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                callback.imageLoadedFail(errorMessage);

            }
        });
    }

    public static void loadFile(final Context context,FilesBean filesBean, final FileCallback callback) {
        getFilesRepository().getFile(context,filesBean, new FilesDataSource.GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                callback.loadFileOk(file);
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {
                callback.loadFileFail(errorMessage);
            }
        });
    }

    public static void loadLogo(Context context,int reqWidth, int reqHeight, List<OrganizationLogosBean> logosBeen, ImageCallback callback) {
        FilesBean filesBean = new FilesBean();
        filesBean.setFileId(logosBeen.get(0).getFileId());
        filesBean.setUrl(logosBeen.get(0).getUrl());
        filesBean.setMimeType("image/*");
        loadImage(context,reqWidth,reqHeight,filesBean, callback);
    }

    public static void loadLogo(Context context,final ImageView imageView, List<OrganizationLogosBean> logosBeen, final ImageCallback callback) {
        FilesBean filesBean = new FilesBean();
        filesBean.setFileId(logosBeen.get(0).getFileId());
        filesBean.setUrl(logosBeen.get(0).getUrl());
        filesBean.setMimeType("image/*");
        loadImage(context,imageView,filesBean, callback);
    }

    public static void loadLogo(final Context context,final ImageView imageView, List<OrganizationLogosBean> logosBeen) {
        FilesBean filesBean = new FilesBean();
        filesBean.setFileId(logosBeen.get(0).getFileId());
        filesBean.setUrl(logosBeen.get(0).getUrl());
        filesBean.setMimeType("image/*");
        getFilesRepository().getFile(context,filesBean, new FilesDataSource.GetFilesCallback() {
            @Override
            public void onFileLoaded(File file) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), options);
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                imageView.measure(w, h);
                int height = imageView.getMeasuredHeight();
                int width = imageView.getMeasuredWidth();
                options.inSampleSize = getFitInSampleSize(width, height, options);
                options.inJustDecodeBounds = false;
                ThreadPool.execute(new UpdateImageSourceTask(file,options,imageView,new Handler(Looper.getMainLooper())));
            }

            @Override
            public void onDataNotAvailable(String errorMessage) {

            }
        });
    }

    public static int getScaleSampleSizeForWidth(int reqWidth, BitmapFactory.Options options) {
        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth;

        if (reqWidth == 0 || height == 0) {
            return 1;
        }
        if (width > reqWidth) {
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    public static int getFitInSampleSize(int reqWidth, int reqHeight, BitmapFactory.Options options) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        int inSampleSize = 1;
        final int height = options.outHeight;
        final int width = options.outWidth;
        if (options.outWidth > reqWidth || options.outHeight > reqHeight) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

}
