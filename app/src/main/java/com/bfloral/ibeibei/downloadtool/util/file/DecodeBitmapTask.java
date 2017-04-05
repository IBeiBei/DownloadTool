package com.bfloral.ibeibei.downloadtool.util.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import java.io.File;

/**
 * Created by MingLi on 11/30/16.
 */

public class DecodeBitmapTask implements Runnable {
    private File file;
    private ImageCallback callback;
    private BitmapFactory.Options options;
    private Handler handler;

    public DecodeBitmapTask(File file, BitmapFactory.Options options, ImageCallback callback, Handler handler){
        this.file = file;
        this.callback = callback;
        this.options = options;
        this.handler = handler;
    }
    @Override
    public void run() {
        final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (bitmap!=null){
                    callback.imageLoadedOk(bitmap);
                }else{
                    callback.imageLoadedFail("");
                }
            }
        });
    }
}
