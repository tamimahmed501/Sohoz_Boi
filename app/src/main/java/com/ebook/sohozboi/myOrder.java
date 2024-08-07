package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class myOrder extends AppCompatActivity {

    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    LottieAnimationView lottieAnimationView;
    MyAdapter myAdapter;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        listView = findViewById(R.id.listView);
        lottieAnimationView = findViewById(R.id.lottie);
        back = findViewById(R.id.back);

        myAdapter = new MyAdapter();
        listView.setAdapter(myAdapter);

        myBook();

        back.setOnClickListener(v -> {
            onBackPressed();
            Animatoo.animateSwipeRight(myOrder.this);
        });
    }

    private void myBook() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        String url = "https://sohozboi-server.vercel.app/api/v1/order/get-my-orders";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    lottieAnimationView.setVisibility(View.GONE);
                    Log.d("Volley", "Response received: " + response.toString());
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray books = data.getJSONArray("books");

                            for (int i = 0; i < books.length(); i++) {
                                JSONObject book = books.getJSONObject(i);
                                String bookId = book.getString("bookId");
                                String id = book.getString("_id");
                                String name = book.getString("name");
                                String coverImage = book.getString("coverImage");

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("bookId", bookId);
                                hashMap.put("name", name);
                                hashMap.put("images", coverImage);

                                arrayList.add(hashMap);
                            }
                            myAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("Volley", "Response indicates failure: " + response.toString());
                        }
                    } catch (JSONException e) {
                        Log.e("Volley", "Error parsing JSON response: " + e.getMessage());
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
                    }
                    lottieAnimationView.setVisibility(View.GONE);
                    Log.e("Volley", "Error fetching data: " + errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                SharedPreferences sharedPreferencesx = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                String authToken = sharedPreferencesx.getString("authToken", "");
                headers.put("Authorization", authToken);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(myOrder.this);
        requestQueue.add(jsonObjectRequest);
    }

    private class MyAdapter extends BaseAdapter {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getLayoutInflater().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.mypurchase, parent, false);
            }

            TextView bookName = convertView.findViewById(R.id.bookname);
            ImageView bookCover = convertView.findViewById(R.id.bookcover);
            CardView card = convertView.findViewById(R.id.card);

            HashMap<String, String> hashMap = arrayList.get(position);
            String name = hashMap.get("name");
            String images = hashMap.get("images");
            String bookID = hashMap.get("bookId");



            bookName.setText(name);

            String imageUrl = "https://shohozboi.s3.us-east-1.amazonaws.com/" + images;
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.loading2)
                    .error(R.drawable.loading2)
                    .into(bookCover);


            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    findbook(bookID, bookCover);




                }
            });

            return convertView;
        }
    }


    private void findbook(String bookID, ImageView bookcover) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        String url = "https://sohozboi-server.vercel.app/api/v1/book/get/" + bookID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    lottieAnimationView.setVisibility(View.GONE);

                    Log.d("Volley", "Response received: " + response.toString()); // Log the raw response
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject data = response.getJSONObject("data");

                            // Extract the required fields
                            String name = data.getString("name");
                            int pages = data.getInt("pages");
                            String bookType = data.getString("bookType");
                            String pdfLink = data.optString("pdfLink", ""); // Use optString with default value
                            String txtLink = data.optString("txtLink", ""); // Use optString with default value

                            // Log the extracted values
                            Log.d("BookDetails", "Name: " + name);
                            Log.d("BookDetails", "Pages: " + pages);
                            Log.d("BookDetails", "Book Type: " + bookType);
                            Log.d("BookDetails", "PDF Link: " + pdfLink);
                            Log.d("BookDetails", "TXT Link: " + txtLink);


                            if (txtLink.length()>10){


                                pdfread2.TEXTLINK="https://shohozboi.s3.us-east-1.amazonaws.com/"+txtLink;
                                pdfread2.BOOKNAME=name;



                                Bitmap bitmap = ((BitmapDrawable) bookcover.getDrawable()).getBitmap();
                                pdfread2.Mybitmap = bitmap;
                                startActivity(new Intent(myOrder.this, pdfread2.class));
                                Animatoo.animateSwipeLeft(myOrder.this);


                            } else {

                                pdfWebView.PDFLINK2="https://shohozboi.s3.us-east-1.amazonaws.com/"+pdfLink;
                                pdfWebView.BOOKNAME=name;
                                startActivity(new Intent(myOrder.this, pdfWebView.class));
                                Animatoo.animateSwipeLeft(myOrder.this);



                            }





                        } else {
                            Log.e("Volley", "Response indicates failure: " + response.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Volley", "Error parsing JSON response: " + e.getMessage());
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
                    }
                    lottieAnimationView.setVisibility(View.GONE);
                    Log.e("Volley", "Error fetching data: " + errorMessage);
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();

                headers.put("x-api-key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGVyZV9pc19hX3NlY3JldCIsIm5hbWUiOiJuYWhpZF9oYXNzYW5fYnVsYnVsIiwiaWF0IjoxNTE2MjM5MDIyLCJhdXRob3IiOiJuYWhpZGhhc3NhbiJ9.iTGyvQVlvqv_Z-R5ZKn7mNKJoR6oT_RglbMxvU-XPM0");
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(myOrder.this);
        requestQueue.add(jsonObjectRequest);
    }







    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(myOrder.this);
    }
}
