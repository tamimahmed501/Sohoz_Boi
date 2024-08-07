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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.ebook.sohozboi.databinding.ActivityBookdetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class bookdetails extends AppCompatActivity {

    ActivityBookdetailsBinding binding;


    private boolean isExpanded = false;

    private String fullText;
    private String shortText;


    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    Myadapter myadapter;


    private static final String PREF_NAME = "MyPrefs";

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
        binding.price.setText(PRICE+" ৳");
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

                    int bookprice = Integer.getInteger(PRICE);

                    if (bookprice==0){

                        buyfreeBook();


                    } else {


                        Toast.makeText(bookdetails.this, "Add to cart first", Toast.LENGTH_SHORT).show();



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


                SharedPreferences sharedPreferences1 = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

                String status = sharedPreferences1.getString("status", "");


                if (status.contains("login")){

                    addCart();


                } else {




                    // Start MainActivity with an Intent
                    Intent intent = new Intent(bookdetails.this, MainActivity.class);

                    // Add a flag to indicate navigation to Offer fragment
                    intent.putExtra("NAVIGATE_TO_ACCOUNT", true);

                    startActivity(intent);
                    Animatoo.animateSwipeLeft(bookdetails.this);


                }








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
                SharedPreferences sharedPreferencesx = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
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
        binding.lottie.setVisibility(View.VISIBLE);

        String url = "https://sohozboi-server.vercel.app/api/v1/book/get-all?limit=11&categories="+CATAGORYID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {

                    binding.lottie.setVisibility(View.GONE);

                    Log.d("Volley", "Response received: " + response.toString()); // Log the raw response
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray books = data.getJSONArray("books");

                            for (int i = 0; i < books.length(); i++) {
                                JSONObject book = books.getJSONObject(i);


                                String bookId = book.getString("bookId");
                                String id = book.getString("_id");
                                String name = book.getString("name");
                                String description = book.getString("description");
                                String views = book.getString("views");
                                String pages = book.getString("pages");
                                String bookType = book.getString("bookType");
                                String pdfLink = book.optString("pdfLink", ""); // Use optString with default value
                                String coverImage = book.getString("coverImage");
                                String txtLink = book.optString("txtLink", ""); // Use optString with default value
                                String readAbit = book.getString("readABit");

                                JSONArray categories = book.getJSONArray("categories");
                                String categoryId = categories.getJSONObject(0).getString("_id");
                                String categoryName = categories.getJSONObject(0).getString("name");

                                JSONArray authors = book.getJSONArray("author");
                                String authorId = authors.getJSONObject(0).getString("_id");
                                String authorName = authors.getJSONObject(0).getString("name");

                                JSONObject publisher = book.getJSONObject("publisher");
                                String publisherId = publisher.getString("_id");
                                String publisherName = publisher.getString("name");

                                JSONObject price = book.getJSONObject("price");
                                String bdt = price.getString("bdt");
                                String usd = price.getString("usd");


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", id);
                                hashMap.put("price", bdt);
                                hashMap.put("dollar", usd);
                                hashMap.put("name", name);
                                hashMap.put("author", authorName);
                                hashMap.put("authorid", authorId);
                                hashMap.put("images", coverImage);
                                hashMap.put("publisher", publisherName);
                                hashMap.put("publisherid", publisherId);
                                hashMap.put("categoryname", categoryName);
                                hashMap.put("categoryid", categoryId);
                                hashMap.put("txtlink", txtLink);
                                hashMap.put("pdflink", pdfLink);
                                hashMap.put("readabit", readAbit);
                                hashMap.put("description", description);
                                hashMap.put("views", views);
                                hashMap.put("booktype", bookType);
                                hashMap.put("pages", pages);

                                arrayList.add(hashMap);
                            }
                            myadapter.notifyDataSetChanged();
                        } else {
                            Log.e("Volley", "Response indicates failure: " + response.toString());
                            binding.lottie.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Volley", "Error parsing JSON response: " + e.getMessage());
                        binding.lottie.setVisibility(View.GONE);

                    }
                },
                error -> {
                    String errorMessage = "";
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        errorMessage += "Status Code: " + statusCode + "\n";
                        try {
                            errorMessage += "Response Data: " + new String(error.networkResponse.data, "UTF-8");
                        } catch (Exception e) {
                            errorMessage += "Error parsing network response data";
                        }
                    } else {
                        errorMessage += "Network response is null";
                        binding.lottie.setVisibility(View.GONE);

                    }
                    Log.e("Volley", "Error fetching data: " + errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("x-api-key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGVyZV9pc19hX3NlY3JldCIsIm5hbWUiOiJuYWhpZF9oYXNzYW5fYnVsYnVsIiwiaWF0IjoxNTE2MjM5MDIyLCJhdXRob3IiOiJuYWhpZGhhc3NhbiJ9.iTGyvQVlvqv_Z-R5ZKn7mNKJoR6oT_RglbMxvU-XPM0");
                return headers;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(bookdetails.this);
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
                viewx = layoutInflater.inflate(R.layout.booklayout, parent, false);
            }

            TextView price, name, author;
            CardView card;
            ImageView images;
            LinearLayout mainlay;

            price = viewx.findViewById(R.id.price);
            name = viewx.findViewById(R.id.name);
            author = viewx.findViewById(R.id.author);
            card = viewx.findViewById(R.id.card);
            images = viewx.findViewById(R.id.images);
            mainlay = viewx.findViewById(R.id.mainlay);

            HashMap<String,String> hashMap = arrayList.get(position);
            String id1 = hashMap.get("id");
            String name1 = hashMap.get("name");
            String author1 = hashMap.get("author");
            String images1 = hashMap.get("images");
            String price1 = hashMap.get("price");
            String dollar1 = hashMap.get("dollar");
            String authorId = hashMap.get("authorid");
            String publisher = hashMap.get("publisher");
            String publisherid = hashMap.get("publisherid");
            String categoryName = hashMap.get("categoryname");
            String categoryId = hashMap.get("categoryid");
            String txtlink = hashMap.get("txtlink");
            String pdflink = hashMap.get("pdflink");
            String readAbit = hashMap.get("readabit");
            String description = hashMap.get("description");
            String views = hashMap.get("views");
            String bookType = hashMap.get("booktype");
            String pages = hashMap.get("pages");


            if (price1.contains("0")) {
                price.setText("ফ্রি বই");
            } else {
                price.setText(price1 + " ৳");
            }

            name.setText(name1);
            author.setText(author1);


            String urlx = "https://shohozboi.s3.us-east-1.amazonaws.com/" + images1;

            Picasso.get()
                    .load(urlx)
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.loading2)
                    .into(images);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {




                    bookdetails.BOOKNAME=name1;
                    bookdetails.AUTHOR=author1;
                    bookdetails.PRICE=price1;
                    bookdetails.PDFLINK=pdflink;
                    bookdetails.TXTLINK=txtlink;
                    bookdetails.CATAGORY=categoryName;
                    bookdetails.DOLLER=dollar1;
                    bookdetails.DESCRIPTION=description;
                    bookdetails.READABIT=readAbit;
                    bookdetails.PAGES=pages;
                    bookdetails.BOOKID=id1;



                    Bitmap bitmap = ((BitmapDrawable) images.getDrawable()).getBitmap();
                    bookdetails.Mybitmap = bitmap;
                    startActivity(new Intent(bookdetails.this, bookdetails.class));
                    Animatoo.animateFade(bookdetails.this);





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

    private void buyfreeBook(){




                // URL of the API
                String url = "https://sohozboi-server.vercel.app/api/v1/order/create/free";

                // Create JSON object for the request body
                JSONObject requestBody = new JSONObject();
                try {
                    requestBody.put("books", new JSONArray(Arrays.asList(BOOKID)));
                    requestBody.put("totalAmount", "0");
                    requestBody.put("currency", "USD");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create a new request queue
                RequestQueue queue = Volley.newRequestQueue(bookdetails.this);

                // Create a new JsonObjectRequest
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                try {
                                    int statusCode = response.getInt("statusCode");
                                    String message = response.getString("message");


                                    if (statusCode==201){



                                        startActivity(new Intent(bookdetails.this, free_buy_success.class));
                                        Animatoo.animateSwipeLeft(bookdetails.this);
                                        finish();
                                    } else {


                                        Toast.makeText(bookdetails.this, "Something Wrong", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            String data = new String(error.networkResponse.data);
                            Log.e("VolleyError", "Status Code: " + statusCode);
                            Log.e("VolleyError", "Data: " + data);
                            Toast.makeText(bookdetails.this, "Something Wrong 2", Toast.LENGTH_SHORT).show();

                        }
                    }

                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        SharedPreferences sharedPreferencesx = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        String authToken = sharedPreferencesx.getString("authToken", "");
                        headers.put("Authorization", authToken); // Replace with your actual token
                        //headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };

                // Add the request to the request queue
                queue.add(jsonObjectRequest);




























    }

}
