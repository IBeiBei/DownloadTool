package com.bfloral.ibeibei.downloadtool.domain;

import android.support.annotation.NonNull;

import com.bfloral.ibeibei.downloadtool.util.EntityUtils;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import retrofit2.Response;

public class ResultError {

    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Error {
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static <T> ResultError buildError(@NonNull Response<T> response) {
        try {
            return EntityUtils.gson.fromJson(response.errorBody().string(), ResultError.class);
        } catch (IOException | JsonSyntaxException e) {
            ResultError resultError = new ResultError();
            Error error = resultError.new Error();
            switch (response.code()) {
                case 400:
                    error.message = "Request parameter error";
                    break;
                case 403:
                    error.message = "Request is refused";
                    break;
                case 404:
                    error.message = "Resource is not found";
                    break;
                case 405:
                    error.message = "Request is not allowed";
                    break;
                case 408:
                    error.message = "Request timeout";
                    break;
                case 422:
                    error.message = "Request semantic error";
                    break;
                case 500:
                    error.message = "Server logic error";
                    break;
                case 502:
                    error.message = "Server gateway error";
                    break;
                case 504:
                    error.message = "Server gateway timeout";
                    break;
                default:
                    error.message = response.message();
                    break;
            }
            resultError.setError(error);
            return resultError;
        }
    }

    public static ResultError buildError(@NonNull Throwable t) {
        ResultError resultError = new ResultError();
        Error error = resultError.new Error();
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            error.message = "Network is not available, please check the network settings.";
        } else if (t instanceof SocketTimeoutException) {
            error.message = "network timeout";
        } else if (t instanceof JsonSyntaxException) {
            error.message = "json from error";
        } else if(t instanceof SSLException){
            error.message = "Connection timed out";
        }else {
            error.message = "unknown error：" + t.getLocalizedMessage();
        }
        resultError.setError(error);
        return resultError;
    }

}
