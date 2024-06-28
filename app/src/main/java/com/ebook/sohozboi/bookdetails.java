package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.ebook.sohozboi.databinding.ActivityBookdetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class bookdetails extends AppCompatActivity {

    ActivityBookdetailsBinding binding;


    private boolean isExpanded = false;

    private String fullText;
    private String shortText;


    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    Myadapter myadapter;



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


        if (Mybitmap!=null) binding.images.setImageBitmap(Mybitmap);


        binding.readabittext.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.readabittext.setText(Html.fromHtml(READABIT, Html.FROM_HTML_MODE_COMPACT));

        } else {
            binding.readabittext.setText(Html.fromHtml(READABIT));

        }


        if (PRICE.contains("0")){


            binding.price.setText("ফ্রী বই");
            binding.read.setText("বইটি পড়ূন");

        } else {

            binding.price.setText(PRICE+" ৳");

        }





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




        binding.images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
                Animatoo.animateSwipeRight(bookdetails.this);
            }
        });







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
                    startActivity(new Intent(bookdetails.this, bookdetails.class));
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
