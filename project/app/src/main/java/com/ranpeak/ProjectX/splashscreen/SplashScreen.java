package com.ranpeak.ProjectX.splashscreen;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ranpeak.ProjectX.R;
import com.ranpeak.ProjectX.activity.lobby.LobbyActivity;
import com.ranpeak.ProjectX.activity.logIn.LogInActivity;
import com.ranpeak.ProjectX.constant.Constants;
import com.ranpeak.ProjectX.dto.TaskDTO;
import com.ranpeak.ProjectX.request.RequestHandler;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private final static int SPLASH_ACTIVITY = R.layout.activity_splash_screen;
    private TextView textView;
    private ImageView imageView;
    private TextView connectionStatus;
    private List<TaskDTO> data = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(SPLASH_ACTIVITY);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById();
        animate();
    }

    @Override
    public void finish() {
        startActivity(new Intent(SplashScreen.this, LogInActivity.class));
//        Intent i = new Intent(this, LobbyActivity.class);
//        i.putExtra("data", (Serializable) data);

        super.finish();
    }

    @Override
    public void onBackPressed() {
        end();
    }

    private void end() {
        super.finish();
    }

    private void findViewById() {
        textView = findViewById(R.id.splash_text);
        imageView = findViewById(R.id.splash_image);
        connectionStatus = findViewById(R.id.splash_textView);
    }

    private void animate() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        textView.startAnimation(animation);
        imageView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                connectionStatus.setVisibility(View.VISIBLE);
                getAllLogins();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void startLoadData() {
        finish();
    }


    private void noInternetConnection() {
        connectionStatus.setText(getString(R.string.internet_check));
        connectionStatus.setTextColor(getResources().getColor(R.color.colorAccent));
    }


    private void getAllLogins() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL.GET_LOGINS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Data downloading", Toast.LENGTH_SHORT).show();
                        startLoadData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Waiting...", Toast.LENGTH_SHORT).show();
                        noInternetConnection();
                    }
                });
        RequestHandler.getmInstance(this).addToRequestQueue(stringRequest);
    }



    public class GetFreeTask extends AsyncTask<Void, Void, List<TaskDTO>> {

        @Override
        protected List<TaskDTO> doInBackground(Void... params) {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List<TaskDTO>> response = restTemplate.exchange(
                    Constants.URL.GET_ALL_TASK,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TaskDTO>>() {
                    });
            List<TaskDTO> taskDTOS = response.getBody();

            return taskDTOS;
        }

        @Override
        protected void onPostExecute(List<TaskDTO> taskDTOS) {
            data = taskDTOS;
            Log.d("Data Size", String.valueOf(data.size()));

        }


    }
}
