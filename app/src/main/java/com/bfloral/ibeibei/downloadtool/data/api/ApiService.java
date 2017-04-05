package com.bfloral.ibeibei.downloadtool.data.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * need define by the actual situation
 */
public interface ApiService {

    @GET("rest/{param1}/rest2/{imageId}")
    Call<ResponseBody> getImage(@Header("Authorization") String token, @Path("param1") String param1, @Path("imageId") String imageId);

    @GET("rest/{param1}/rest2/{attachmentId}")
    Call<ResponseBody> getAttachment(@Header("Authorization") String token, @Path("param1") String param1, @Path("attachmentId") String attachmentIdj);

    @GET
    Call<ResponseBody> downLoadFileWithDynamicUrl(@Url String fileUrl);
}
