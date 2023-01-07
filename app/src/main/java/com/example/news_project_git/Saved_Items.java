package com.example.news_project_git;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.news_project_git.Models.Articles;
import com.example.news_project_git.Models.HelperClass;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Saved_Items extends AppCompatActivity {
    ListView listView;

    List<Articles> list;

    DatabaseReference myRef;

    listAdapter adapter;

    ProgressBar progress_bar;

    CardView home,meteo,favoris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_items);

        listView = (ListView) findViewById(R.id.savelist);

        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        list = new ArrayList<>();

        myRef = FirebaseDatabase.getInstance("https://newsapp-25c3a-default-rtdb.europe-west1.firebasedatabase.app//").getReference(new HelperClass().getMacAddr());

        adapter = new listAdapter((ArrayList<Articles>) list);

        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren() ){
                    Articles a = postSnapshot.getValue(Articles.class);
                    a.setKey(postSnapshot.getKey());
                    list.add(a);
                }
                adapter.notifyDataSetChanged();

                progress_bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
                progress_bar.setVisibility(View.INVISIBLE);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String uri = list.get(position).getUrl();

                Intent intent = new Intent(Saved_Items.this,item_description.class);

                intent.putExtra("url",uri);

                startActivity(intent);
            }
        });





        home = (CardView) findViewById(R.id.home);

        favoris = (CardView)findViewById(R.id.favoris);

        meteo =(CardView) findViewById(R.id.meteo);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Saved_Items.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        meteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Saved_Items.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });


    }
}