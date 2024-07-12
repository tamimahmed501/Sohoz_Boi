package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
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
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
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


    int totalamount = 0;

    public static String CURRENCY = "BDT";

    public static String BOOKID = "";

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




        // Set up a listener to handle radio button click events
        binding.radioGroupCurrency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button was clicked
                if (checkedId == R.id.radioButtonBDT) {
                    // Handle BDT radio button click
                    // For example, update some UI or perform an action
                    binding.radioButtonBDT.setChecked(true); // Ensure it stays checked
                    binding.radioButtonUSD.setChecked(false); // Uncheck other radio button if needed

                    CURRENCY="BDT";

                } else if (checkedId == R.id.radioButtonUSD) {
                    // Handle USD radio button click
                    // For example, update some UI or perform an action
                    binding.radioButtonBDT.setChecked(false); // Uncheck other radio button if needed
                    binding.radioButtonUSD.setChecked(true); // Ensure it stays checked

                    CURRENCY="USD";
                }
            }
        });
        binding.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                depositmethod.CURRENCY = CURRENCY;
                depositproccess.BOOKID=BOOKID;
                depositproccess.CURRENCY=CURRENCY;

               String amountx = Integer.toString(totalamount);

                depositproccess.AMOUNT=amountx;

                startActivity(new Intent(cartList.this, depositmethod.class));
                Animatoo.animateSwipeLeft(cartList.this);

            }
        });
        getCart();


        myadapter = new Myadapter();
        binding.listView.setAdapter(myadapter);



        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Animatoo.animateSwipeRight(cartList.this);
            }
        });


    }

    private void getCart() {
        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(cartList.this);

        binding.lottie.setVisibility(View.VISIBLE);

        arrayList.clear();
        arrayList = new ArrayList<>();

        // URL for the GET request
        String url = "https://sohozboi-server.vercel.app/api/v1/cart/my-cart";

        // Create the GET request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response

                        binding.lottie.setVisibility(View.GONE);
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray items = data.getJSONArray("items");
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject itemObject = items.getJSONObject(i);
                                    JSONObject bookObject = itemObject.optJSONObject("book");

                                    if (bookObject != null) {
                                        String bookId = bookObject.getString("_id");
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



                                        int totalprice = totalamount + bdt;

                                        binding.amount.setText(String.valueOf(totalprice)+" BDT");








                                    }

                                    myadapter.notifyDataSetChanged();
                                }
                            } else {
                                String message = response.getString("message");

                                binding.lottie.setVisibility(View.GONE);
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

                            binding.lottie.setVisibility(View.GONE);
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
            ImageView coverimages, delete;
            CheckBox checkBox;


            usd = viewx.findViewById(R.id.usd);
            bdt = viewx.findViewById(R.id.bdt);
            author = viewx.findViewById(R.id.author);
            bookname = viewx.findViewById(R.id.bookname);
            coverimages = viewx.findViewById(R.id.coverimages);
            delete = viewx.findViewById(R.id.delete);
            checkBox = viewx.findViewById(R.id.checkbox);

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




            if (checkBox.isChecked()){

                BOOKID = bookId1;

            }


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    deleteitem(bookId1);

                }
            });



            return viewx;
        }
    }


    private void deleteitem(String itemid) {

    }


}