package com.questionpro.oauth_poc;
import static com.questionpro.oauth_poc.Credentials.AUTH0_ENDPOINT_GET_USER_LIST;
import static com.questionpro.oauth_poc.Credentials.AUTH0_CLIENT_BASE_URL;
import static com.questionpro.oauth_poc.Credentials.AUTH0_CLIENT_ID;
import static com.questionpro.oauth_poc.Credentials.AUTH0_CLIENT_SECRET;
import static com.questionpro.oauth_poc.Credentials.AUTH0_TOKEN_URL;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    private TextView resultTextView;
    public static final String TAG = "MainActivity";


    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    resultTextView = findViewById(R.id.tvResult);
    Button authorizeButton = findViewById(R.id.btnAuthorize);


    authorizeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new GetAccessTokenTask().execute();
        }
    });
}

private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + AUTH0_CLIENT_ID
                + "&client_secret=" + AUTH0_CLIENT_SECRET
                + "&audience=" + AUTH0_CLIENT_BASE_URL);
        Request request = new Request.Builder()
                .url(AUTH0_TOKEN_URL)
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                String accessToken = jsonResponse.getString("access_token");
                return accessToken;

            } else {
                Log.e(TAG, "Error getting access token: " + response.code() + " " + response.body().string());
                return null;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String accessToken) {
        if (accessToken != null) {
            resultTextView.setText("Access Token: " + accessToken);
            new CallApiTask(accessToken).execute();
        } else {
            resultTextView.setText("Failed to get access token.");
        }
    }
}

private class CallApiTask extends AsyncTask<Void, Void, String> {

    private final String accessToken;

    CallApiTask(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected String doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(AUTH0_CLIENT_BASE_URL + AUTH0_ENDPOINT_GET_USER_LIST)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.e(TAG, "Error calling API: " + response.code() + " " + response.body().string());
                return "API call failed: " + response.code();
            }
        } catch (IOException e) {
            Log.e(TAG, "API call error: " + e.getMessage());
            return "API call error: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        resultTextView.setText("API Response: " + result);
    }
    }
}



