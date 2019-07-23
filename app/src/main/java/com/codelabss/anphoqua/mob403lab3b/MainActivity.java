package com.codelabss.anphoqua.mob403lab3b;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private String urlJsonObj = "http://192.168.64.2/mob403lab3/person_object.json";
    private String urlJsonArr = "http://192.168.64.2/mob403lab3/person_array.json";
    private static String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog progressDialog;
    private TextView txtResponse;
    private Button btnMakeObjectRequest, getBtnMakeArrayRequest;
    private String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMakeObjectRequest = findViewById(R.id.btnObjRequest);
        getBtnMakeArrayRequest = findViewById(R.id.btnArrRequest);
        txtResponse = findViewById(R.id.txtResponse);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait in a minute...");
        progressDialog.setCancelable(false);

        btnMakeObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonObjectRequest();
            }
        });

        getBtnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJsonArrayRequest();
            }
        });
    }

    private void makeJsonArrayRequest() {
        showDialog();

        final JsonArrayRequest jsonArrayReq = new JsonArrayRequest(urlJsonArr, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                try {
                    jsonResponse = "";
                    for (int i=0; i < response.length(); i++){
                        JSONObject person =  (JSONObject) response.get(i);
                        String name = person.getString("name");
                        String email = person.getString("email");

                        JSONObject phone = person.getJSONObject("phone");
                        String home = phone.getString("home");
                        String mobile = phone.getString("mobile");

                        jsonResponse += "Name: " +name+ "\n";
                        jsonResponse += "Email: " +email+ "\n";
                        jsonResponse += "Home: " +home+ "\n";
                        jsonResponse += "Phone: " +mobile+ "\n\n";
                    }

                    txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error.getMessage());
                Toast.makeText(MainActivity.this, "Error: " +error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayReq);
    }

    private void makeJsonObjectRequest() {
        showDialog();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.GET, urlJsonObj,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                //Parsing json object to response
                //Response will be a json object
                try {
                    String name = response.getString("name");
                    String email = response.getString("email");
                    JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("home");
                    String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse += "Name: " +name+ "\n";
                    jsonResponse += "Email: " +email+ "\n";
                    jsonResponse += "Home: " +home+ "\n";
                    jsonResponse += "Phone: " +mobile+ "\n";

                    txtResponse.setText(jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: "+error.getMessage());
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectReq);
    }

    private void hideDialog() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing()){
            progressDialog.show();
        }
    }


}
