package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebook.sohozboi.databinding.ActivityCartListBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class cartList extends AppCompatActivity {

    ActivityCartListBinding binding;

    ArrayList<HashMap<String, String>>arrayList = new ArrayList<>();
    private static final String PREF_NAME = "MyAppPrefs";
    HashMap<String, String> hashMap = new HashMap<>();

    Myadapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Change the status bar color to white and text to black
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    WindowInsetsController insetsController = decorView.getWindowInsetsController();
                    if (insetsController != null) {
                        insetsController.setSystemBarsAppearance(
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                        );
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        binding = ActivityCartListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getCart();


        myadapter = new Myadapter();
        binding.listView.setAdapter(myadapter);





    }

    private void getCart() {
        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(cartList.this);

        // URL for the GET request
        String url = "https://server.shohozboi.com/api/v1/cart/my-cart";

        // Create the GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray items = data.getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject itemObject = items.getJSONObject(i);
                                    JSONObject bookObject = itemObject.optJSONObject("book");

                                    if (bookObject != null) {
                                        String bookId = bookObject.getString("bookId");
                                        String name = bookObject.getString("name");
                                        String coverImage = bookObject.getString("coverImage");
                                        boolean status = bookObject.getBoolean("status");
                                        int bdt = bookObject.getJSONObject("price").getInt("bdt");
                                        int usd = bookObject.getJSONObject("price").getInt("usd");




                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("bookId", bookId);
                                        hashMap.put("coverimage", coverImage);
                                        hashMap.put("usd", String.valueOf(usd));
                                        hashMap.put("bdt", String.valueOf(bdt));
                                        hashMap.put("name", name);


                                        arrayList.add(hashMap);





                                    }

                                    myadapter.notifyDataSetChanged();
                                }
                            } else {
                                String message = response.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error response
                        error.printStackTrace();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String body = new String(error.networkResponse.data);
                        } else {
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferencesx = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String authToken = sharedPreferencesx.getString("authToken", "");
                headers.put("Authorization", authToken);
                //headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }



    private class Myadapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
        @Override
        public View getView(int position, View viewx, ViewGroup parent) {
            if (viewx == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getLayoutInflater().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                viewx = layoutInflater.inflate(R.layout.cartbook, parent, false);
            }


            TextView usd,bdt,author,bookname;
            CardView card;
            ImageView coverimages;


            usd = viewx.findViewById(R.id.usd);
            bdt = viewx.findViewById(R.id.bdt);
            author = viewx.findViewById(R.id.author);
            bookname = viewx.findViewById(R.id.bookname);
            coverimages = viewx.findViewById(R.id.coverimages);


            HashMap<String,String> hashMap = arrayList.get(position);
            String bookId1 = hashMap.get("bookId");
            String name1 = hashMap.get("name");
            String usd1 = hashMap.get("usd");
            String bdt1 = hashMap.get("bdt");
            String coverimage1 = hashMap.get("coverimage");





            bookname.setText(name1);
            author.setText("Unknown");
            bdt.setText(bdt1+" ৳");
            usd.setText(usd1+" $");


            String urlx = "https://shohozboi.s3.us-east-1.amazonaws.com/"+coverimage1;



            Picasso.get()
                    .load(urlx)
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.load2)
                    .into(coverimages);








            return viewx;
        }
    }


}