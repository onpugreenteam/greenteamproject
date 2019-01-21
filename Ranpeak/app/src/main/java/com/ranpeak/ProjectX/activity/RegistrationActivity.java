package com.ranpeak.ProjectX.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ranpeak.ProjectX.R;
import com.ranpeak.ProjectX.constant.Constants;
import com.ranpeak.ProjectX.entities.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText register_login;
    private EditText register_password;
    private EditText register_name;
    private EditText register_age;
    private EditText register_country;
    private EditText register_gender;

    private Button register_button;

    private ImageView IconInRegister;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        register_login = (EditText) findViewById(R.id.register_login);
        register_password = (EditText) findViewById(R.id.register_password);
        register_name = (EditText) findViewById(R.id.register_name);
        register_age = (EditText) findViewById(R.id.register_age);
        register_country = (EditText) findViewById(R.id.register_country);
        register_gender = (EditText) findViewById(R.id.register_gender);

        register_button = (Button) findViewById(R.id.register_register);

        progressDialog = new ProgressDialog(this);
        register_button.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v==register_button){
            registerUser();
        }

    }

    private void registerUser(){
        final String login = register_login.getText().toString().trim();
        final String password = register_password.getText().toString().trim();
        final String name = register_name.getText().toString().trim();
        final String age = register_age.getText().toString().trim();
        final String country = register_country.getText().toString().trim();
        final String gender = register_gender.getText().toString().trim();


        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL.POST_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("login",login);
                params.put("password",password);
                params.put("name", name);
                params.put("age", age);
                params.put("country", country);
                params.put("gender", gender);
                return params;
            }

            };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


//     private class  RESTTask extends AsyncTask<String, Void, ResponseEntity<User>> {
//
//        protected ResponseEntity<User> doInBackground(String... uri) {
//
//            final String url = uri[0];
//            RestTemplate restTemplate = new RestTemplate();
//
//            try{
//                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//
//                HttpHeaders headers = new HttpHeaders();
//
//                headers.set("APIKey", "5567GGH67225HYVGG");
//
//                String auth = "Jack" + ":" + "Jones";
//
//                String encodeAuth = Base64.encodeToString(auth.getBytes(), Base64.DEFAULT);
//                String authHeader = "Basic" + new String(encodeAuth);
//                headers.set("Authorization", authHeader);
//
//                HttpEntity<String> entity = new HttpEntity<String>(headers);
//
//                ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
//
//                return response;
//
//        }catch (Exception ex){
//
//                String message = ex.getMessage();
//                return null;
//            }
//        }
//
//        protected void OnPostExecute(ResponseEntity<User> result){
//            HttpStatus statusCode = result.getStatusCode();
//            User userReturned = result.getBody();
//        }
//
//    }
//
//    public  void sendMessage(View view){
//        final String uri = "http://localhost:8080/getallusers/1";
//        new RESTTask().execute(uri);
//    }

}
