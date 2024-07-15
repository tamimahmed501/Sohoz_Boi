package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.ebook.sohozboi.databinding.ActivityBookdetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bookdetails extends AppCompatActivity {

    ActivityBookdetailsBinding binding;


    private boolean isExpanded = false;

    private String fullText;
    private String shortText;


    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    Myadapter myadapter;

    private static final String PREF_NAME = "MyAppPrefs";

    ImageView addcart;

    public static String BOOKNAME = "";
    public static String BOOKID = "";
    public static String AUTHOR = "";
    public static String AUTHORID = "";
    public static String PUBLISHER = "";
    public static String PUBLISHERID = "";
    public static String PRICE = "";
    public static String DOLLER = "";
    public static String CATAGORY = "";
    public static String CATAGORYID = "";
    public static String TXTLINK = "";
    public static String PDFLINK = "";
    public static String READABIT = "";
    public static String DESCRIPTION = "";
    public static String VIEWS = "";
    public static String BOOKTYPE = "";
    public static String PAGES = "";
    public static Bitmap Mybitmap = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_bookdetails);
        binding = ActivityBookdetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);




        myadapter = new Myadapter();
        binding.gridView.setAdapter(myadapter);




        binding.bookname.setText(BOOKNAME);
        binding.booktitle.setText(BOOKNAME);
        binding.doller.setText(DOLLER+" $");
        binding.author.setText(AUTHOR);
        binding.category.setText(CATAGORY);
        binding.page.setText(PAGES+" পৃষ্ঠা");

        if (Mybitmap!=null) binding.images.setImageBitmap(Mybitmap);


        binding.readabittext.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.readabittext.setText(Html.fromHtml(READABIT, Html.FROM_HTML_MODE_COMPACT));

        } else {
            binding.readabittext.setText(Html.fromHtml(READABIT));

        }




            binding.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (TXTLINK.length()>10){



                        pdfread2.TEXTLINK="https://shohozboi.s3.us-east-1.amazonaws.com/"+TXTLINK;
                        pdfread2.BOOKNAME=BOOKNAME;



                        Bitmap bitmap = ((BitmapDrawable) binding.images.getDrawable()).getBitmap();
                        pdfread2.Mybitmap = bitmap;
                        startActivity(new Intent(bookdetails.this, pdfread2.class));
                        Animatoo.animateSwipeLeft(bookdetails.this);

                    } else {


                        pdfWebView.PDFLINK2="https://shohozboi.s3.us-east-1.amazonaws.com/"+PDFLINK;
                        pdfWebView.BOOKNAME=BOOKNAME;
                        startActivity(new Intent(bookdetails.this, pdfWebView.class));
                        Animatoo.animateSwipeLeft(bookdetails.this);


                    }






                }
            });




        binding.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(bookdetails.this, cartList.class));
                Animatoo.animateSwipeLeft(bookdetails.this);

            }
        });




        binding.readabit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.readabittext.getVisibility()==View.VISIBLE){

                    binding.readabittext.setVisibility(View.GONE);
                    binding.abit.setText("আংশিক পড়ুন");

                } else {

                    binding.readabittext.setVisibility(View.VISIBLE);
                    binding.abit.setText("বন্ধ করুন");
                }

            }
        });


        newBooks();



        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        binding.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Animatoo.animateSwipeRight(bookdetails.this);
            }
        });



        binding.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                addCart();






            }
        });




    }

    private void addCart() {
        // Initialize Volley request queue
        RequestQueue requestQueue = Volley.newRequestQueue(bookdetails.this);

        // URL for the POST request
        String url = "https://sohozboi-server.vercel.app/api/v1/cart/create";

        // Create the JSON object for the request body
        JSONObject requestBody = new JSONObject();
        try {
            JSONArray bookIds = new JSONArray();
            bookIds.put(BOOKID);
            requestBody.put("bookIds", bookIds);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Create the POST request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Toast.makeText(bookdetails.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(bookdetails.this, "Already Added", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(bookdetails.this, "Already Added", Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(bookdetails.this, "Already Added", Toast.LENGTH_SHORT).show();
                        
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferencesx = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                String authToken = sharedPreferencesx.getString("authToken", "");
                headers.put("Authorization", authToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        requestQueue.add(jsonObjectRequest);
    }


    private void newBooks() {

        arrayList.clear();





        String url = "https://wikipediabangla.com/apps/sohozboi/booklist.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {





                    for (int x = 0; x < response.length(); x++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(x);
                            String id = jsonObject.getString("id");
                            String price = jsonObject.getString("price");
                            String dollar = jsonObject.getString("dollar");
                            String name = jsonObject.getString("name");
                            String author = jsonObject.getString("author");
                            String images = jsonObject.getString("images");




                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", id);
                            hashMap.put("price", price);
                            hashMap.put("dollar", dollar);
                            hashMap.put("name", name);
                            hashMap.put("author", author);
                            hashMap.put("images", images);


                            arrayList.add(hashMap);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Notify the adapter that the data set has changed
                    myadapter.notifyDataSetChanged();
                },
                error -> {
                    // Handle error response
                    Toast.makeText(bookdetails.this, "Error Response: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(bookdetails.this);
        requestQueue.add(jsonArrayRequest);
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
                viewx = layoutInflater.inflate(R.layout.booklayout, parent, false);
            }


            TextView price,name,author;
            CardView card;
            ImageView images;


            price = viewx.findViewById(R.id.price);
            name = viewx.findViewById(R.id.name);
            author = viewx.findViewById(R.id.author);
            card = viewx.findViewById(R.id.card);
            images = viewx.findViewById(R.id.images);


            HashMap<String,String> hashMap = arrayList.get(position);
            String id1 = hashMap.get("id");
            String name1 = hashMap.get("name");
            String author1 = hashMap.get("author");
            String images1 = hashMap.get("images");
            String price1 = hashMap.get("price");
            String dollar1 = hashMap.get("dollar");




            name.setText(name1);
            author.setText(author1);
            price.setText(price1+" ৳");



            String urlx = "https://wikipediabangla.com/apps/Images/"+images1;



            Picasso.get()
                    .load(images1)
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.load2)
                    .into(images);


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(bookdetails.this, "Demo Book", Toast.LENGTH_SHORT).show();
                }
            });






            return viewx;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSwipeRight(bookdetails.this);
    }
}
