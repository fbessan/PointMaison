package com.bescorp.pointmaison;

import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by fbessan on 15/06/2017.
 */

public interface APIService {


    @GET("index.php")
    Call<String> setPointData(@Query("who") String who,@Query("what") String what,@Query("amount") String amount,@Query("datecreated") String datecreated);



}
