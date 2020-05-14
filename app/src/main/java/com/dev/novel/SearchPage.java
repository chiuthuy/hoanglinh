package com.dev.novel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.novel.Adapters.NovelAdapter;
import com.dev.novel.Models.Novel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    private EditText searchBar;
    private FirebaseFirestore db;
    private ListView listOfNovels;
    private Button searchButton, homePageButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.search_page);
        initState();

        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomePage();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoad();
            }
        });
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        searchBar = (EditText) findViewById(R.id.searchBar);
        searchButton = (Button) findViewById(R.id.searchButton);
        listOfNovels = (ListView) findViewById(R.id.listOfNovels);
        homePageButton = (Button) findViewById(R.id.homePageButton);
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(SearchPage.this, HomePage.class);
        startActivity(intent);
        finish();
    }

    private void onLoad() {
        String key = searchBar.getText().toString();
        searchNovel(key);
    }

    private void searchNovel(final String name) {
        CollectionReference novels = db.collection("novels");
        novels.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Novel> novels = new ArrayList<>();
                    Novel[] novelArray = {};

                    if (task.getResult() != null) {
                        for (QueryDocumentSnapshot novelSnapshot: task.getResult()) {
                            Novel novel = snapshotToNovel(novelSnapshot);
                            if (novel.getName().toLowerCase().contains(name.toLowerCase())) {
                                novels.add(novel);
                            }
                        }

                        novelArray = novels.toArray(novelArray);
                        renderNovels(novelArray);
                    }
                }
            }
        });
    }

    private void renderNovels(final Novel[] novels) {
        NovelAdapter adapter = new NovelAdapter(SearchPage.this, novels);
        listOfNovels.setAdapter(adapter);
    }

    private Novel snapshotToNovel(QueryDocumentSnapshot snapshot) {
        return new Novel(snapshot.getId(), snapshot.getString("name"));
    }
}
