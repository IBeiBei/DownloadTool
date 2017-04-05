package com.bfloral.ibeibei.downloadtool.data.api;

/**
 * Created by MingLi on 4/5/17.
 */

public class BaseUrl {
    //The server url (self service || the third part service)
    //If the url is static can define here else can define before use
    public static String url = "";

    public static final long READ_TIME_OUT = 60_000;
    public static final long WRITE_TIME_OUT = 60_000;
    public static final long CONNECT_TIME_OUT = 60_000;
}
