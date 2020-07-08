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

public class SignUpActivity extends AppCompatActivity {
    Button signup;
    EditText username, password,confirmpassword,email;
    TextView txtLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = (EditText)findViewById(R.id.txtuser);
        password = (EditText)findViewById(R.id.txtpass);
        confirmpassword = (EditText)findViewById(R.id.txtconfirmpass);
        email = (EditText)findViewById(R.id.txtemail);
        signup = (Button)findViewById(R.id.btnsignUp);
        txtLogin=(TextView)findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateData())
                    SignUp();
            }
        });
    }
    private void SignUp() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Contants.URL_REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                  //  JSONArray array = obj.getJSONArray("message");

                    if (!obj.getBoolean("error")) {

                       /* for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobj = array.getJSONObject(i);
                        }*/
                      //  Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString());
                 params.put("email", email.getText().toString());
                params.put("role", "user");
                params.put("token", FirebaseInstanceId.getInstance().getToken());
                return params;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    private boolean ValidateData() {
        if(username.getText().toString().isEmpty())
        {
            username.setError("Username Required with no space");
            return false;
        }
        else if(password.getText().toString().isEmpty())
        {
            password.setError("Password Required");
            return false;
        }
        else if(email.getText().toString().isEmpty())
        {
            email.setError("Email Required");
            return false;
        }
        else if(!password.getText().toString().equals(confirmpassword.getText().toString()))
        {
            confirmpassword.setError("Password do not match");
            return false;
        }
        return  true;
    }
}