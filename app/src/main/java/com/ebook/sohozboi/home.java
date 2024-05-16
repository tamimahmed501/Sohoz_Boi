package com.ebook.sohozboi;

import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class home extends Fragment {


    ImageSlider imageSlider;

    ArrayList<HashMap<String,String>>arrayList = new ArrayList<>();

    Myadapter myadapter;
    RecyleAdapter recyleAdapter;
    GridView gridView;
    RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_home, container, false);

        imageSlider = myView.findViewById(R.id.image_slider);
        gridView = myView.findViewById(R.id.gridView);
        recyclerView = myView.findViewById(R.id.recyCcearView);


        myadapter = new Myadapter();
        gridView.setAdapter(myadapter);

        recyleAdapter = new RecyleAdapter();
        recyclerView.setAdapter(recyleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));



        imageSlider();



        newBooks();





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
                    recyleAdapter.notifyDataSetChanged();
                },
                error -> {
                    // Handle error response
                    Toast.makeText(getContext(), "Error Response: " + error.toString(), Toast.LENGTH_SHORT).show();
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                    startActivity(new Intent(getContext(), bookdetails.class));
                }
            });






            return viewx;
        }
    }

    private class RecyleAdapter extends RecyclerView.Adapter<RecyleAdapter.MyViewHolder>{

        private class MyViewHolder extends RecyclerView.ViewHolder{
            TextView price, name, author;
            CardView card;
            ImageView images;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                price = itemView.findViewById(R.id.price);
                name = itemView.findViewById(R.id.name);
                author = itemView.findViewById(R.id.author);
                card = itemView.findViewById(R.id.card);
                images = itemView.findViewById(R.id.images);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View myView = layoutInflater.inflate(R.layout.booklayout2, parent, false);
            return new MyViewHolder(myView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            HashMap<String,String> hashMap = arrayList.get(position);
            String id1 = hashMap.get("id");
            String name1 = hashMap.get("name");
            String author1 = hashMap.get("author");
            String images1 = hashMap.get("images");
            String price1 = hashMap.get("price");
            String dollar1 = hashMap.get("dollar");

            holder.name.setText(name1);
            holder.author.setText(author1);
            holder.price.setText(price1 + " ৳");

            String urlx = "https://wikipediabangla.com/apps/Images/" + images1;

            Picasso.get()
                    .load(images1)
                    .placeholder(R.drawable.load2)
                    .error(R.drawable.load2)
                    .into(holder.images);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }


}