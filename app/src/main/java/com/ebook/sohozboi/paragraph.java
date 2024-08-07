package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class paragraph extends AppCompatActivity implements androidx.appcompat.widget.SearchView.OnQueryTextListener {

    LottieAnimationView lottie;
    private static final String TAG = "Paragraph";
    private static final String URL = "https://server.shohozboi.com/api/v1/paragraph/get-all?skipLimit=YES";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0aGVyZV9pc19hX3NlY3JldCIsIm5hbWUiOiJuYWhpZF9oYXNzYW5fYnVsYnVsIiwiaWF0IjoxNTE2MjM5MDIyLCJhdXRob3IiOiJuYWhpZGhhc3NhbiJ9.iTGyvQVlvqv_Z-R5ZKn7mNKJoR6oT_RglbMxvU-XPM0";

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> originalList = new ArrayList<>(); // Store original data

    RecyclerView recyclerView;
    MyAdapter myAdapter;
    androidx.appcompat.widget.SearchView searchView; // Use the correct type

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paragraph);

        lottie = findViewById(R.id.lottie);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);

        lottie.setVisibility(View.VISIBLE);

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(paragraph.this));
        searchView.setOnQueryTextListener(this);

        fetchParagraphs();
    }

    private void fetchParagraphs() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create the JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        lottie.setVisibility(View.GONE);
                        Toast.makeText(paragraph.this, "Data Retrieved", Toast.LENGTH_SHORT).show();
                        // Handle the response
                        Log.d(TAG, "Response: " + response.toString());

                        try {
                            JSONObject dataObject = response.getJSONObject("data");
                            JSONArray paragraphsArray = dataObject.getJSONArray("paragraphs");

                            for (int x = 0; x < paragraphsArray.length(); x++) {
                                JSONObject jsonObject = paragraphsArray.getJSONObject(x);
                                String title = jsonObject.getString("title");
                                String content = jsonObject.getString("content");
                                String meaning = jsonObject.getString("meaning");


                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("title", title);
                                hashMap.put("content", content);
                                hashMap.put("meaning", meaning);


                                arrayList.add(hashMap); // Add to arrayList
                            }
                            originalList.addAll(arrayList); // Store original data

                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(paragraph.this, "Failed to parse data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(TAG, "Error: " + error.toString());
                        lottie.setVisibility(View.GONE);
                        Toast.makeText(paragraph.this, "Load Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("x-api-key", API_KEY);
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    // Implement the onQueryTextSubmit method
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Implement the onQueryTextChange method
    @Override
    public boolean onQueryTextChange(String newText) {
        myAdapter.filter(newText); // Call the filter method in your adapter
        return true;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.paragraph, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            HashMap<String, String> hashMap = arrayList.get(position);

            String title1 = hashMap.get("title");
            String content = hashMap.get("content");
            String meaning = hashMap.get("meaning");

            holder.title.setText(title1);

            holder.mainlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    readparagraph.TITLE=title1;
                    readparagraph.CONTENT=content;
                    readparagraph.MEANING=meaning;

                    startActivity(new Intent(paragraph.this, readparagraph.class));
                    Animatoo.animateSwipeLeft(paragraph.this);

                }
            });

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        // Filter method to filter data based on user's input
        public void filter(String text) {
            arrayList.clear();
            if (text.isEmpty()) { // If the search query is empty, show all data
                arrayList.addAll(originalList); // Restore original data
            } else {
                for (HashMap<String, String> item : originalList) {
                    if (item.get("title").toLowerCase().contains(text.toLowerCase())) {
                        arrayList.add(item);
                    }
                }
            }
            notifyDataSetChanged();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            RelativeLayout mainlay;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                mainlay = itemView.findViewById(R.id.mainlay);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        finishAndRemoveTask();
        Animatoo.animateSwipeRight(paragraph.this);
    }
}
