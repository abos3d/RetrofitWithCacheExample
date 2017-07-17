package com.tecandmore.retrofitexample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    Button button;

    Call<JsonObject> call;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        button = (Button) findViewById(R.id.button);

        createNetworkComponents();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRequest();
            }
        });
    }

    private void createNetworkComponents() {

        /**Create cache*/
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(getApplication().getCacheDir(), cacheSize);
        /**cache created*/


        /**create okhttp3 object*/
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .cache(cache)//adding the cache object that we have created
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder request = originalRequest.newBuilder();
                        if (false)
                            request.cacheControl(CacheControl.FORCE_NETWORK);//Here you can pass FORCE_NETWORK parameter to avoid getting response from our cache

                        /**if want to control cache timeout you can use this*/
                        request.cacheControl(new CacheControl.Builder()
                                .maxAge(15, TimeUnit.MINUTES)
                                .build());

                        return chain.proceed(request.build());
                    }
                });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fixer.io/") // that means base url + the left url in interface "http://api.fixer.io/latest"
                .client(okHttpClientBuilder.build())//adding okhttp3 object that we have created
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        call = retrofit.create(ApiInterface.class).getResponse();
    }


    private void startRequest() {
        pd = ProgressDialog.show(this, "", "Loading", true, false);

        if (call.isExecuted())
            call = call.clone();

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                int statusCode = response.code();
                JsonObject responseJsonObject = response.body();
                textView.setText("Response Code: " + statusCode + "\n\n\n" + "Response: \n" + responseJsonObject);
                pd.dismiss();
                call.cancel();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Log error here since request failed
                Log.e("MainActivity", t.toString());
                Toast.makeText(MainActivity.this, "Error has occurred: \n" + t.toString(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });
    }
}
