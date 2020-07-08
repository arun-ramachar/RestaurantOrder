package com.restaurant.restaurantorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class DineIn extends AppCompatActivity {


    IntentIntegrator qrCode;
    EditText orderList,table;
    String orderType="";
    Button btnOrder,btnScan;
    ACProgressFlower dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dine_in);
        qrCode=new IntentIntegrator(this);
        orderList=(EditText)findViewById(R.id.et_instructions);
        table=(EditText)findViewById(R.id.et_table_no);
        btnOrder = (Button)findViewById(R.id.btnPlaceOrder);
        btnScan = (Button)findViewById(R.id.btnScan);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!orderList.getText().toString().isEmpty())
                {
                    PlaceOrder("Dine In Request:\n"+orderList.getText().toString()+"\n"+"Table No:"+table.getText().toString());
                    showDialogCustom("Please wait...");
                }
            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCode.initiateScan();
            }
        });
    }
    private void showDialogCustom(String s) {
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(s)
                .fadeColor(Color.DKGRAY).build();
        dialog.show();
    }

    private void PlaceOrder(final String order) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Contants.URL_PLACE_ORDER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    // JSONArray array = obj.getJSONArray("message");

                    if (!obj.getBoolean("error")) {

                        dialog.dismiss();

                        showSuccessDialog(obj.getString("message")+", Please wait for your order");
                        orderList.setText("");
                        sendNotificationToAdmin();

                    }
                    else

                    {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException exp)
                {

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
                params.put("username", SharedPrefManager.getUsername(getApplicationContext()));
                params.put("id", SharedPrefManager.getID(getApplicationContext()));
                params.put("order", order);
                params.put("status","Recieved");
                return params;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void sendNotificationToAdmin() {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Contants.URL_GET_ADMIN_TOKEN,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    // JSONArray array = obj.getJSONArray("message");

                    if (!obj.getBoolean("error")) {

                        //dialog.dismiss();
                        NotifyAdmin(obj.getString("token"));



                    }
                    else

                    {
                        Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException exp)
                {

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

                params.put("role","admin");
                return params;

            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void NotifyAdmin(String token) {
        String message="You have new order recieved";
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "http://ktldb.com/restaurantOrder/firebase_notify.php?send_notification&message="+message+"&token="+token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();
                // startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                //finish();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showSuccessDialog(String s) {
        // Create Alert using Builder
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Success")
                .setMessage(s)
                .addButton("OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, (dialog, which) -> {
                    // Toast.makeText(MainActivity.this, "Upgrade tapped", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });

// Show the alert
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                   // JSONObject obj = new JSONObject(result.getContents());
                   // orderList.setText(orderList.getText().toString()+"\n"+obj.getString("name")+"   Price-  "+obj.getString("price")+"  "+orderType);
                    orderList.setText( orderList.getText().toString()+"\n"+result.getContents()+"/Dine-In");
                    //setting values to textviews
                    // textViewName.setText(obj.getString("name"));
                    //textViewAddress.setText(obj.getString("price"));
                } catch (Exception e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}