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

    @GET("latest")//here is the left url part
    Call<JsonObject> getResponse();//this function you have the option to name it all you need to take care is the return Object


    //in case you want to use a path parameter or query parameter this commented code might help :)
//    @GET("someUrl/{id}")
//    Call<SomeResponse> getSomeCall(@Path("id") int id, @Query("queryId") String someQuery);
}
