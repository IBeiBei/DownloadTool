package com.bfloral.ibeibei.downloadtool.util.file;

import android.graphics.Bitmap;

/**
 * Created by MingLi on 10/27/16.
 */

public interface ImageCallback {
    void imageLoadedOk(Bitmap bitmap);
    void imageLoadedFail(String message);
}
