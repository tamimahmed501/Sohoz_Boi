package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class bookdetails extends AppCompatActivity {

    ImageView imageView;

    private boolean isExpanded = false;
    private TextView textView, readMoreTextView;
    private String fullText;
    private String shortText;


    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    Myadapter myadapter;
    GridView gridView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookdetails);

        imageView = findViewById(R.id.back);
        textView = findViewById(R.id.textView);
        readMoreTextView = findViewById(R.id.readMoreTextView);


        gridView = findViewById(R.id.gridView);


        myadapter = new Myadapter();
        gridView.setAdapter(myadapter);

        newBooks();


        // Assume fullText is your long text
        fullText = "Traffic jam is the situation when vehicles are stopped completely for some time period on the roads. Also, vehicles have to wait for a long time to move out of the jam. Sometimes it becomes like congestion in traffic. This happens in transport network due to the increasing vehicles and overuse of roads. Often it is due to slow speed, longer trip time and increased queues of vehicles. Therefore, traffic jam is becoming a major issue mostly in all cities.";

        // Create the short text by taking the first 100 characters
        shortText = fullText.substring(0, Math.min(fullText.length(), 100)) + "...";

        // Set the initial text to shortText
        textView.setText(shortText);

        textView.post(new Runnable() {
            @Override
            public void run() {
                // Ensure the layout is ready
                readMoreTextView.setVisibility(View.VISIBLE);
            }
        });

        readMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleText();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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


    private void toggleText() {
        if (isExpanded) {
            textView.setText(shortText);
            readMoreTextView.setText("Read more");
        } else {
            textView.setText(fullText);
            readMoreTextView.setText("See less");
        }
        isExpanded = !isExpanded;
    }
}
