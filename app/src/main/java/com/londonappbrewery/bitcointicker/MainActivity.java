package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {

    // Constants:
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";
    private final  String CRYPTO = "BTC";

    // Member Variables:
    TextView mPriceTextView;
    private String mCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        final Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Set the OnItemClickListener for the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCurrency =  (String) spinner.getItemAtPosition(position);
                Log.d("Bitcoin", "The selected currency is: " + mCurrency);


                String url = BASE_URL+CRYPTO+mCurrency;
                letsDoSomeNetworking(url);
                Log.d("Bitcoin", "The URL is: " + url);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(MainActivity.this, "You must select a currency", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                Log.d("Bitcoin", "Success! The JSON is: " + response.toString());

                try {
                    String Rate = response.getJSONObject("open").getString("hour");
                    mPriceTextView.setText(Rate);
                    Log.d("Bitcoin", "The price is: " + Rate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Log.e("Bitcoin", "FAILURE!");
                Log.e("Bitcoin", "The error code is: " + statusCode);
                Toast.makeText(MainActivity.this, "Failed to connect!", Toast.LENGTH_LONG).show();
            }
        });
    }


}
