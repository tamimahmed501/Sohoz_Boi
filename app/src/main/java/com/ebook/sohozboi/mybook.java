package com.ebook.sohozboi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.HashMap;

public class mybook extends Fragment {

    ListView listView;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    Myadapter myadapter;
    DatabaseHelper dbHelper;

    RelativeLayout mybook;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_mybook, container, false);

        mybook = myView.findViewById(R.id.mybook);
        listView = myView.findViewById(R.id.listView);
        dbHelper = new DatabaseHelper(getContext());
        loadBooksFromDatabase();

        myadapter = new Myadapter(getContext(), arrayList);
        listView.setAdapter(myadapter);


        mybook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getContext(), myOrder.class));
                Animatoo.animateSwipeLeft(getContext());





            }
        });

        return myView;
    }

    private void loadBooksFromDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> book = new HashMap<>();
                book.put("id", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                book.put("name", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BOOK_NAME)));
                book.put("author", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.AUTHOR_NAME))); // Corrected to COLUMN_AUTHOR
                book.put("images", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE_BITMAP)));
                arrayList.add(book);
            }
            cursor.close();
        }
        db.close();
    }


    private class Myadapter extends BaseAdapter {
        private Context context;
        private ArrayList<HashMap<String, String>> arrayList;

        public Myadapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint({"MissingInflatedId", "ResourceAsColor"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.item_mybook, parent, false);
            }

            TextView bookname = convertView.findViewById(R.id.bookname);
            TextView author = convertView.findViewById(R.id.author);
            TextView purchasedate = convertView.findViewById(R.id.purchasedate);
            ImageView bookcover = convertView.findViewById(R.id.bookcover);
            CardView card = convertView.findViewById(R.id.card);
            ImageView delete = convertView.findViewById(R.id.delete);

            HashMap<String, String> hashMap = arrayList.get(position);
            String id1 = hashMap.get("id");
            String name1 = hashMap.get("name");
            String author1 = hashMap.get("author");
            String images1 = hashMap.get("images");

            bookname.setText(name1);
            author.setText(author1);

            // Decode the base64 image string to a bitmap and set it to the ImageView
            if (images1 != null && !images1.isEmpty()) {
                byte[] decodedString = Base64.decode(images1, Base64.DEFAULT);
                bookcover.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
            } else {
                bookcover.setImageResource(R.drawable.loading2); // Set a default image if no image is found
            }

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pdfread2.BOOKNAME = name1;
                    startActivity(new Intent(context, pdfread2.class));
                    Animatoo.animateSwipeLeft(getContext());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteBookFromDatabase(id1);
                }
            });

            return convertView;
        }

        private void deleteBookFromDatabase(String id) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = DatabaseHelper.COLUMN_ID + " = ?";
            String[] selectionArgs = { id };

            int deletedRows = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
            if (deletedRows > 0) {
                // Book deleted successfully
                arrayList.clear(); // Clear the list and reload data
                loadBooksFromDatabase();
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Book deleted successfully", Toast.LENGTH_SHORT).show();
            }

            db.close();
        }
    }

}
