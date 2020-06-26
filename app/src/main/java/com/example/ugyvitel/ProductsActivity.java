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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class ProductsActivity extends AppCompatActivity {

    private List<Product> products = new ArrayList<Product>();
    LinearLayout scp;
    BottomNavigationView bottomNavigationView;
    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products2);
        init();
        products.clear();
        Picasso.get().setLoggingEnabled(true);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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
                scp.removeAllViews();
                for (final Product p:products) {
                    LinearLayout layout = new LinearLayout(getApplicationContext());
                    ImageView img = new ImageView(getApplicationContext());
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edit = prefs.edit();
                            edit.putInt("product_id", p.getId());
                            edit.putString("product_name", p.getName());
                            edit.putString("product_description", p.getDescription());
                            edit.putString("product_plaintext", p.getPlaintext());
                            edit.putString("product_icon", p.getIcon());
                            edit.putInt("product_price", p.getPrice());
                            edit.commit();
                            Intent view = new Intent(ProductsActivity.this, ViewProduct.class);
                            startActivity(view);
                            finish();
                        }
                    });
                    img.setLayoutParams(wrap);
                    Picasso.get().load(p.getIcon()).into(img);
                    layout.setLayoutParams(margin);
                    if (p.isPurchasable())
                    {
                        layout.setBackground(getDrawable(R.drawable.customer_card));
                    }
                    else
                    {
                        layout.setBackground(getDrawable(R.drawable.product_card));
                    }
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    TextView text0 = new TextView(getApplicationContext());
                    text0.setLayoutParams(linear);
                    text0.setTextSize(25);
                    text0.setText(p.toString());
                    layout.addView(img);
                    layout.addView(text0);
                    scp.addView(layout);
                }
            }
        }, 1000);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.btnCustomers:
                        Intent refresh = new Intent(ProductsActivity.this, MainActivity.class);
                        startActivity(refresh);
                        finish();
                        break;
                    case R.id.btnProducts:
                        Intent products = new Intent(ProductsActivity.this, ProductsActivity.class);
                        startActivity(products);
                        finish();
                        break;
                }
                return true;
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams wrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                margin.setMargins(0,20,0,20);
                scp.removeAllViews();
                for (final Product p:products) {
                    if (p.getName().contains(search.getText().toString()) || p.getPlaintext().contains(search.getText().toString()) || p.getDescription().contains(search.getText().toString()) || String.valueOf(p.getPrice()).contains(search.getText().toString()))
                    {
                        LinearLayout layout = new LinearLayout(getApplicationContext());
                        ImageView img = new ImageView(getApplicationContext());
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putInt("product_id", p.getId());
                                edit.putString("product_name", p.getName());
                                edit.putString("product_description", p.getDescription());
                                edit.putString("product_plaintext", p.getPlaintext());
                                edit.putString("product_icon", p.getIcon());
                                edit.putInt("product_price", p.getPrice());
                                edit.commit();
                                Intent view = new Intent(ProductsActivity.this, ViewProduct.class);
                                startActivity(view);
                                finish();
                            }
                        });
                        img.setLayoutParams(wrap);
                        Picasso.get().load(p.getIcon()).into(img);
                        layout.setLayoutParams(margin);
                        if (p.isPurchasable())
                        {
                            layout.setBackground(getDrawable(R.drawable.customer_card));
                        }
                        else
                        {
                            layout.setBackground(getDrawable(R.drawable.product_card));
                        }
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        TextView text0 = new TextView(getApplicationContext());
                        text0.setLayoutParams(linear);
                        text0.setTextSize(25);
                        text0.setText(p.toString());
                        layout.addView(img);
                        layout.addView(text0);
                        scp.addView(layout);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void init()
    {
        search = findViewById(R.id.search);
        scp = findViewById(R.id.scp);
        bottomNavigationView = findViewById(R.id.bottomNavigationProducts);
    }
}
