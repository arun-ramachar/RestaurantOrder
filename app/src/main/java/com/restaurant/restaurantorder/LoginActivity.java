package com.restaurant.restaurantorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button login;
    EditText username, password;
    TextView txtCreate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Shared Prefrence is just like local database i.e sqlite.

        //when user is login first time his or her details are saved in shared prefrences,
        // so that next time we can send the user directly to MainActivity.

        //get user id from sharedprefrence if not null
        if(!SharedPrefManager.getID(getApplicationContext()).isEmpty())
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
     //intializing views
        username = (EditText)findViewById(R.id.txtuser);
        password = (EditText)findViewById(R.id.txtpass);
        login = (Button)findViewById(R.id.btnlogin);
        txtCreate=(TextView)findViewById(R.id.txtCreateAccount);
        txtCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //starting new screen or activity on create account click
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verify user login details
                LoginButton();
            }
        });

    }
    public void LoginButton(){

        //Volle String request to perform network requestto verify user login details from details
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Contants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                  //  JSONArray array = obj.getJSONArray("message");

                    if (!obj.getBoolean("error")) {

                       /* for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobj = array.getJSONObject(i);
                        }*/

                       //saving user id,username,firebase token to shared prefence
                       SharedPrefManager.setID(obj.getString("id"),getApplicationContext());
                        SharedPrefManager.setUsername(obj.getString("username"),getApplicationContext());
                        SharedPrefManager.setToken(obj.getString("token"),getApplicationContext());
                        //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }
                }catch (JSONException exp)
                {
                    Toast.makeText(getApplicationContext(),exp.getMessage(),Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            //Binding parameters username and password
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString());
                     return params;

            }
        };
        //adding request to queue to perform network request for login verification
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

    }
}