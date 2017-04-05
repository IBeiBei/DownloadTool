package com.bfloral.ibeibei.downloadtool.data.api;


import com.bfloral.ibeibei.downloadtool.domain.ResultError;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundCallback<T> implements Callback<T>, ICallback<T> {

    @Override
    public final void onResponse(Call<T> call, Response<T> response) {
        boolean interrupt;
        if (response.isSuccessful()) {
            interrupt = onResultOk(response.code(), response.headers(), response.body());
        } else {
            interrupt = onResultError(response.code(), response.headers(), ResultError.buildError(response));
        }
        if (!interrupt) {
            onFinish();
        }
    }

    @Override
    public final void onFailure(Call<T> call, Throwable t) {
        boolean interrupt;
        if (call.isCanceled()) {
            interrupt = onCallCancel();
        } else {
            interrupt = onCallException(t, ResultError.buildError(t));
        }
        if (!interrupt) {
            onFinish();
        }
    }

    @Override
    public boolean onResultOk(int code, Headers headers, T result) {
        return false;
    }

    @Override
    public boolean onResultError(int code, Headers headers, ResultError error) {
        return false;
    }

    @Override
    public boolean onCallCancel() {
        return false;
    }

    @Override
    public boolean onCallException(Throwable t, ResultError error) {
        return false;
    }

    @Override
    public void onFinish() {}

}
