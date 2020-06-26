package com.example.ugyvitel;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ViewProduct extends AppCompatActivity {

    private TextView tv1, tv2, tv3, tv4;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        Picasso.get().setLoggingEnabled(true);
        init();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent menu = new Intent(ViewProduct.this, ProductsActivity.class);
                startActivity(menu);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        tv1.setText(prefs.getString("product_name", "null"));
        tv2.setText(prefs.getString("product_description", "null").replace(".","\n"));
        tv3.setText(prefs.getString("product_plaintext", "null"));
        tv4.setText(String.valueOf(prefs.getInt("product_price", 0)));
        Picasso.get().load(prefs.getString("product_icon","null")).into(img);
    }

    public void init()
    {
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        img = findViewById(R.id.img_product);
    }
}
