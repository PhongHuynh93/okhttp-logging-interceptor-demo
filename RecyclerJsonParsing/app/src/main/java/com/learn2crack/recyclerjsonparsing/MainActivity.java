package com.learn2crack.recyclerjsonparsing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @see <a href="https://www.learn2crack.com/2016/06/retrofit-okhttp-logging-interceptor.html"></a>
 * Sometimes while working with Retrofit and parsing JSON data you may get some weird errors similar to this,
 * Error :JsonReader.setLenient(true)to accpect malformed JSON at line 1 Column 1 path $
 * The problem is the JSON response is not valid so that the Gson converter cannot serialize it.
 *
 * -> ta phải có cách để xem JSON tra về, có 2 cách
 * 1. One is to use Postman to make the request and check the response
 * 2. the other is to attach Okhttp logging interceptor to Retrofit and check the logs.
 *
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AndroidVersion> data;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: 8/14/16 2 when oncreate, init view
        initViews();
    }
    private void initViews(){
        recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        // TODO: 8/14/16 3 should be onstart to load the connection 
        loadJSON();
    }

    private void loadJSON(){
        // TODO: 8/14/16 4 create client 
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        
        // TODO: 8/14/16 5 create a new HttpLoggingInterceptor and set logging level,
        // BODY prints everything.
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // TODO: 8/14/16 6 Then add the interceptor to Okhttp client,
        client.addInterceptor(loggingInterceptor);

        // TODO: 8/14/16 7 Finally add the Okhttp client to Retrofit using client() method.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.learn2crack.com")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponse> call = request.getJSON();
        call.enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                // TODO: 8/14/16 test this json response before add to adapter
//
//                JSONResponse jsonResponse = response.body();
//                data = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
//                adapter = new DataAdapter(data);
//                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }
}