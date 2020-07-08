package com.restaurant.restaurantorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends AppCompatActivity {

    private ImageButton dinein;
    private ImageButton takeaway;
    IntentIntegrator qrCode;
    EditText orderList;
    String orderType="";
    Button btnOrder;
    ACProgressFlower dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intializing views
        qrCode=new IntentIntegrator(this);
        dinein = (ImageButton) findViewById(R.id.dinein);
        takeaway = (ImageButton) findViewById(R.id.takeaway);
        orderList=(EditText)findViewById(R.id.txtOrderList);
        btnOrder = (Button)findViewById(R.id.btnPlaceOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!orderList.getText().toString().isEmpty())
                {
                    //sending order details to admin
                    PlaceOrder(orderList.getText().toString());
                    //show waiting dialog..
                    showDialogCustom("Please wait...");
                }
            }
        });
        dinein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  qrCode.initiateScan();
              //  orderType="DIN";

                //satrt dine in screen or activity
                startActivity(new Intent(MainActivity.this,DineIn.class));
               // orderList.setText("User Requested for DINE IN\n");
                //openQRScan();
            }
        });

        takeaway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //initalizing qrscan to perform qr reading
                qrCode.initiateScan();
                //set order type
                orderType="TAWAY";

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu for logout funtion at screen top right corner so that user logout from the app
        getMenuInflater().inflate(R.menu.more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_logout){
            //removing user details from shared prefence
            SharedPrefManager.setID(null,getApplicationContext());
            SharedPrefManager.setUsername(null,getApplicationContext());
            SharedPrefManager.setToken(null,getApplicationContext());
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
            // do something
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialogCustom(String s) {
        //waiting dialog...custom github library
         dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(s)
                .fadeColor(Color.DKGRAY).build();
         dialog.show();
    }

    private void PlaceOrder(final String order) {
        //sending details to database requesting admin...
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
                       /* new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Are you sure?")
                                .setContentText("Won't be able to recover this file!")
                                .setConfirmText("Yes,delete it!")
                                .show();*/
                       /* for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobj = array.getJSONObject(i);
                        }*/
                        //Toast.makeText(getApplicationContext(),obj.getString("message"),Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                        //finish();

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
        //get admin firebase token in order to send notification
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Contants.URL_GET_ADMIN_TOKEN,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    // JSONArray array = obj.getJSONArray("message");

                    if (!obj.getBoolean("error")) {

                        //send notification to admin
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

    //send actual notification
    private void NotifyAdmin(String token) {

        //notification message
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

    //this method is called when scaning is complete
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
                  //  JSONObject obj = new JSONObject(result.getContents());
                    //orderList.getText().toString()+"\n"+obj.getString("name")+"   Price-  "+obj.getString("price")+"\n"+obj.getString("url")
                    orderList.setText( orderList.getText().toString()+"\n"+result.getContents()+"/"+orderType);
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