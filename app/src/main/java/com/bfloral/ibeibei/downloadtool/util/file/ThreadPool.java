package com.bfloral.ibeibei.downloadtool.util.file;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by MingLi on 11/30/16.
 */

public class ThreadPool {
    private static final int MAX_THREAD = 5;
    private static ExecutorService INSTANCE = null;

    private ThreadPool() {
    }

    private static ExecutorService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = Executors.newFixedThreadPool(MAX_THREAD);
        }
        return INSTANCE;
    }

    public static void execute(Runnable command) {
        getInstance().execute(command);
    }
}
