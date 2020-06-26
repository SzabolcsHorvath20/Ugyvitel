package com.example.ugyvitel;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout sc;
    private List<Customer> customers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        customers.clear();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://10.0.2.2:3000/customers/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] splitf = response.split(Pattern.quote("},"));
                        for (String f:splitf) {
                            String[] split = f.split(",");
                            for (String s:split) {
                                s.replace('"',' ');
                            }
                            String[] s0 = split[0].split(":");
                            String[] s1 = split[1].split(":");
                            String[] s2 = split[2].split(":");
                            String[] s3 = split[3].split(":");
                            int id = Integer.parseInt(s0[1].trim());
                            String name = s1[1].replace('"', ' ').trim();
                            String email = s2[1].replace('"', ' ').trim();
                            String phone = s3[1].replace('"', ' ').replace('}', ' ').replace(']',' ').trim();
                            customers.add(new Customer(id, name, email, phone));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "error.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                margin.setMargins(0,20,0,20);
                sc.removeAllViews();
                for (final Customer c:customers) {
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putInt("customer_id", c.getId());
                            edit.commit();
                            Intent products = new Intent(MainActivity.this, NewOrderActivity.class);
                            startActivity(products);
                            finish();
                        }
                    });
                    layout.setLayoutParams(margin);
                    layout.setBackground(getDrawable(R.drawable.customer_card));
                    layout.setOrientation(LinearLayout.VERTICAL);
                    TextView text0 = new TextView(context);
                    text0.setLayoutParams(linear);
                    text0.setTextSize(25);
                    text0.setText(c.toString());
                    layout.addView(text0);
                    sc.addView(layout);
                }
            }
        }, 1000);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.btnCustomers:
                        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(refresh);
                        finish();
                        break;
                    case R.id.btnProducts:
                        Intent products = new Intent(MainActivity.this, ProductsActivity.class);
                        startActivity(products);
                        finish();
                        break;
                    case R.id.btn_add_customer:
                        SharedPreferences prefs = context.getSharedPreferences("prefs",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putInt("count", customers.size());
                        edit.commit();
                        Intent add_customer = new Intent(MainActivity.this, AddCustomerActivity.class);
                        startActivity(add_customer);
                        finish();
                        break;
                }
                return true;
            }
        });

    }

    public void init()
    {
        sc = findViewById(R.id.sc);
        bottomNavigationView = findViewById(R.id.bottomNavigationMenu);
    }

}
