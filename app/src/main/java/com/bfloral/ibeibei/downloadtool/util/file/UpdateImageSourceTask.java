package com.bfloral.ibeibei.downloadtool.util.file;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by MingLi on 11/30/16.
 */

public class UpdateImageSourceTask implements Runnable {
    private File file;
    private ImageView imageView;
    private BitmapFactory.Options options;
    private Handler handler;

    public UpdateImageSourceTask(File file, BitmapFactory.Options options, ImageView imageView, Handler handler){
        this.file = file;
        this.imageView = imageView;
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
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
    }
}
