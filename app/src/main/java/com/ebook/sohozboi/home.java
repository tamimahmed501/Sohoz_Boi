package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class home extends Fragment {


    ImageSlider imageSlider;

    ArrayList<HashMap<String,String>>arrayList = new ArrayList<>();

    Myadapter myadapter;
    GridView gridView;

    LottieAnimationView lottie;
    LinearLayout paragraph;
    CardView search;

    ImageView cart;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = myView.findViewById(R.id.image_slider);
        gridView = myView.findViewById(R.id.gridView);
        lottie = myView.findViewById(R.id.lottie);
        paragraph = myView.findViewById(R.id.paragraph);
        search = myView.findViewById(R.id.search);
        cart = myView.findViewById(R.id.cart);

        myadapter = new Myadapter();
        gridView.setAdapter(myadapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), booksearch.class));
                Animatoo.animateSwipeLeft(getContext());
            }
        });



        imageSlider();



        newBooks();




        paragraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), paragraph.class));
                Animatoo.animateSwipeLeft(getContext());


            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), cartList.class));
                Animatoo.animateSwipeLeft(getContext());





            }
        });


        return myView;


    }

    private void imageSlider(){






        String urlx = "https://wikipediabangla.com/apps/sohozboi/imageslider.php";

        JsonArrayRequest jsonArrayRequestx = new JsonArrayRequest(
                com.android.volley.Request.Method.GET, urlx, null,
                response -> {



                    for (int x = 0; x < response.length(); x++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(x);
                            String image1 = jsonObject.getString("image1");
                            String image2 = jsonObject.getString("image2");
                            String image3 = jsonObject.getString("image3");
                            String image4 = jsonObject.getString("image4");



                            // ImageSlider initialization and setup
                            ArrayList<SlideModel> imageList = new ArrayList<>();
                            imageList.add(new SlideModel(image1, ScaleTypes.FIT));
                            imageList.add(new SlideModel(image2, ScaleTypes.FIT));
                            imageList.add(new SlideModel(image3, ScaleTypes.FIT));
                            imageList.add(new SlideModel(image4, ScaleTypes.FIT));

                            imageSlider.setImageList(imageList);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                error -> {

                });

        RequestQueue requestQueuex = Volley.newRequestQueue(getContext());
        requestQueuex.add(jsonArrayRequestx);




    }




    private void newBooks() {
        arrayList.clear();

        lottie.setVisibility(View.VISIBLE);
        String url = "https://sohozboi-server.vercel.app/api/v1/book/get-all";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {

                    lottie.setVisibility(View.GONE);

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
                            lottie.setVisibility(View.GONE);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Volley", "Error parsing JSON response: " + e.getMessage());
                        lottie.setVisibility(View.GONE);

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
                        lottie.setVisibility(View.GONE);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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


            if (price1.contains("0")){

                price.setText("ফ্রি বই");


            } else {

                price.setText(price1+" ৳");


            }


            name.setText(name1);
            author.setText(author1);



            String urlx = "https://shohozboi.s3.us-east-1.amazonaws.com/"+images1;



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
                    startActivity(new Intent(getContext(), bookdetails.class));
                    Animatoo.animateSwipeLeft(getContext());





                }
            });






            return viewx;
        }
    }



}