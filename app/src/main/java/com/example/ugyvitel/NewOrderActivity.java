package com.example.ugyvitel;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class NewOrderActivity extends AppCompatActivity {

    public List<Product> products = new ArrayList<Product>();
    LinearLayout sco;
    public HashMap<Integer,Integer> order = new HashMap<>();
    public Button new_order;
    public int counter = 0, ordered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent menu = new Intent(NewOrderActivity.this, MainActivity.class);
                startActivity(menu);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        init();
        Picasso.get().setLoggingEnabled(true);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url1 ="http://10.0.2.2:3000/orders/";
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] splitf = response.split(Pattern.quote("},"));
                        counter = splitf.length/2;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest1);
        String url ="http://10.0.2.2:3000/products/";
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
                            String[] s4 = split[4].split(":");
                            String[] s5 = split[5].split(":");
                            String[] s6 = split[6].split(":");
                            String name = s0[1].replace('"', ' ').trim();
                            String description = s1[1].replace('"', ' ').replace("<stats>","").replace("</stats>","").replace("<mana>","").replace("</mana>","").replace("<groupLimit>","").replace("</groupLimit>","").replace("<unique>","").replace("</unique>","").replace("<br>","").trim();
                            String plaintext = s2[1].replace('"', ' ').trim();
                            int id = Integer.parseInt(s3[1].trim());
                            String icon = (s4[1]+":"+s4[2]).replace('"', ' ').trim();
                            int price = Integer.parseInt(s5[1].replace('"', ' ').trim());
                            Boolean purchasable = Boolean.parseBoolean(s6[1].replace('"', ' ').replace('}', ' ').replace(']',' ').trim());
                            products.add(new Product(name, description, plaintext, id, icon, price, purchasable));

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error.getMessage()", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void run() {
                LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                margin.setMargins(0,20,0,20);
                sco.removeAllViews();
                for (final Product p:products) {
                    order.put(p.getId(),0);
                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    LinearLayout layout2 = new LinearLayout(getApplicationContext());
                    LinearLayout layout3 = new LinearLayout(getApplicationContext());
                    layout2.setLayoutParams(margin);
                    layout3.setLayoutParams(wrap);
                    layout2.setOrientation(LinearLayout.VERTICAL);
                    ImageView img = new ImageView(getApplicationContext());
                    img.setLayoutParams(wrap);
                    Button btnp = new Button(getApplicationContext());
                    btnp.setLayoutParams(wrap);
                    Button btnm = new Button(getApplicationContext());
                    btnm.setLayoutParams(wrap);
                    final TextView et = new TextView(getApplicationContext());
                    et.setTextSize(20);
                    et.setLayoutParams(wrap);
                    btnp.setText("+1");
                    btnm.setText("-1");
                    et.setText("0");
                    btnp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int help = Integer.parseInt(et.getText().toString());
                            et.setText(String.valueOf(help+1));
                            int help2 = order.get(p.getId());
                            order.put(p.getId(),help2+1);
                            ordered+=1;
                        }
                    });
                    btnm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Integer.parseInt(et.getText().toString()) != 0)
                            {
                                int help = Integer.parseInt(et.getText().toString());
                                et.setText(String.valueOf(help-1));
                                int help2 = order.get(p.getId());
                                order.put(p.getId(),help2-1);
                                ordered-=1;
                            }
                        }
                    });
                    Picasso.get().load(p.getIcon()).into(img);
                    layout.setLayoutParams(margin);
                    if (p.isPurchasable())
                    {
                        layout2.setBackground(getDrawable(R.drawable.customer_card));
                    }
                    else
                    {
                        layout2.setBackground(getDrawable(R.drawable.product_card));
                        btnp.setClickable(false);
                        btnm.setClickable(false);
                    }
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView text0 = new TextView(getApplicationContext());
                    text0.setLayoutParams(linear);
                    text0.setTextSize(25);
                    text0.setText(p.toString());
                    layout.addView(img);
                    layout.addView(text0);
                    layout2.addView(layout);
                    layout3.addView(btnp);
                    layout3.addView(et);
                    layout3.addView(btnm);
                    layout2.addView(layout3);
                    sco.addView(layout2);
                }
            }
        }, 1000);

        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ordered > 0)
                {
                    try {
                        JSONObject order_json = new JSONObject();
                        JSONArray jsona = new JSONArray();
                        for (Integer key:order.keySet()) {
                            if (order.get(key) != 0)
                            {
                                JSONObject object = new JSONObject();
                                object.put("id",key);
                                object.put("cnt", order.get(key));
                                jsona.put(object);
                            }
                        }
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs",Context.MODE_PRIVATE);
                        order_json.put("id", counter+1);
                        order_json.put("customerId", prefs.getInt("customer_id",0));
                        order_json.put("products", jsona);
                        String url = "http://10.0.2.2:3000/orders/";
                        final JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, url, order_json, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(NewOrderActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewOrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            public void run() {
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                queue.add(request1);
                            }
                        }, 2000);
                    }
                    catch (JSONException error)
                    {
                        Toast.makeText(NewOrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(NewOrderActivity.this, "Can't save empty order", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public void init()
    {
        sco = findViewById(R.id.sco);
        new_order = findViewById(R.id.new_order);
    }
}
