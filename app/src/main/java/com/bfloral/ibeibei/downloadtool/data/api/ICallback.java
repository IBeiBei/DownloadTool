package com.bfloral.ibeibei.downloadtool.data.api;


import com.bfloral.ibeibei.downloadtool.domain.ResultError;

import okhttp3.Headers;

public interface ICallback<T> {

    boolean onResultOk(int code, Headers headers, T result);

    boolean onResultError(int code, Headers headers, ResultError error);

    boolean onCallCancel();

    boolean onCallException(Throwable t, ResultError error);

    void onFinish();

}
