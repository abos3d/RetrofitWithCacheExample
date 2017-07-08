package com.tecandmore.retrofitexample;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Musab on 08/07/17.
 */

interface ApiInterface {
//
//    @GET("someUrl/{id}")
//    Call<MoviesResponse> getSomeCall(@Path("id") int id, @Query("queryId") String someQuery);

    @GET("latest")
    Call<JsonObject> getResponse();
}
