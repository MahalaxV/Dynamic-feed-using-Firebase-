package com.newboston.hp.rsstest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<NameLinkPair> nameLinkPairs;

    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameLinkPairs = new ArrayList<>();

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fb.getReference("links");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object o = dataSnapshot.getValue();
                ArrayList<HashMap<String, String>> myRetVal = (ArrayList<HashMap<String, String>>) o;
                for(int i = 0; i < myRetVal.size(); i++)
                {
                    HashMap<String, String> nl = myRetVal.get(i);
                    nameLinkPairs.add(new NameLinkPair(nl.get("name"), nl.get("link")));
                }

                RefreshDisplay();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        ArrayList<NameLinkPair> nlps = new ArrayList<>();
//
//        NameLinkPair nlps2[] = new NameLinkPair[3];
//        nlps2[0] = new NameLinkPair("Google", "www.google.com");
//        nlps2[1] = new NameLinkPair("Microsoft", "www.microsoft.com");
//        nlps2[2] = new NameLinkPair("Apple", "www.apple.com");
//
//
//        nlps.add(new NameLinkPair("Google", "www.google.com"));
//        nlps.add(new NameLinkPair("Microsoft", "www.microsoft.com"));
//        nlps.add(new NameLinkPair("Apple", "www.apple.com"));

        myList = (ListView) findViewById(R.id.myList);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse((String) nameLinkPairs.get(position).link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void RefreshDisplay()
    {
        ArrayList<String> myStrList = new ArrayList<>();

        for(int i = 0; i < nameLinkPairs.size(); i++)
        {
            myStrList.add(nameLinkPairs.get(i).name);
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, myStrList);
        myList.setAdapter(myAdapter);
    }
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }
}
