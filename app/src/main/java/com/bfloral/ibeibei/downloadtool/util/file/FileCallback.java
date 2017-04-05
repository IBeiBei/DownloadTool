package com.bfloral.ibeibei.downloadtool.util.file;

import java.io.File;

/**
 * Created by MingLi on 10/27/16.
 */

public interface FileCallback {
    void loadFileOk(File file);
    void loadFileFail(String message);
}
