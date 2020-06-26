package com.example.ugyvitel;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCustomerActivity extends AppCompatActivity {

    private TextInputLayout etEmail, etName, etPhone;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        init();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent menu = new Intent(AddCustomerActivity.this, MainActivity.class);
                startActivity(menu);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etEmail.getEditText().getText().equals("") && !etName.getEditText().getText().equals("") && !etPhone.getEditText().getText().equals(""))
                {
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    String url = "http://10.0.2.2:3000/customers/";
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    int count = prefs.getInt("count", 0);
                    try {
                        JSONObject json = new JSONObject();
                        json.put("id", count+1);
                        json.put("name", etName.getEditText().getText().toString());
                        json.put("email", etEmail.getEditText().getText().toString());
                        json.put("phone", etPhone.getEditText().getText().toString());
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AddCustomerActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(AddCustomerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        queue.add(request);
                    }
                    catch (JSONException error)
                    {
                        Toast.makeText(AddCustomerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                Intent menu = new Intent(AddCustomerActivity.this, MainActivity.class);
                startActivity(menu);
                finish();
            }
        });
    }

    public void init()
    {
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        btn_add = findViewById(R.id.btn_add);
    }
}
