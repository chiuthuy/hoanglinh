package com.dev.novel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.novel.Adapters.NovelAdapter;
import com.dev.novel.Models.Novel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
//    private EditText searchBar;
    private FirebaseFirestore db;
    private ListView listOfNovels;
    private Button logOutButton, searchButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.home_page);
        initState();
        getNovels();

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                navigateToLogInPage();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchPage();
            }
        });
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        logOutButton = (Button) findViewById(R.id.logOutButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        listOfNovels = (ListView) findViewById(R.id.listOfNovels);
    }

    private void getNovels() {
        CollectionReference novels = db.collection("novels");
        getAllNovels(novels);
    }

    private void getAllNovels(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Novel> listOfNovels = new ArrayList<>();
                    Novel[] novels = {};

                    for (QueryDocumentSnapshot novel: task.getResult()) {
                        listOfNovels.add(responseToNovel(novel));
                    }

                    novels = listOfNovels.toArray(novels);
                    renderListOfNovels(novels);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderListOfNovels(final Novel[] novels) {
        NovelAdapter adapter = new NovelAdapter(HomePage.this, novels);
        listOfNovels.setAdapter(adapter);
        listOfNovels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Novel novel = novels[position];
                navigateToNovelPage(novel.getId(), novel.getName());
            }
        });
    }

    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERNAME", MODE_PRIVATE);
        sharedPreferences.edit().remove("username").apply();
    }

    private void navigateToLogInPage() {
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToNovelPage(String novelId, String novelTitle) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(HomePage.this, NovelPage.class);

        bundle.putString("NOVEL_ID", novelId);
        bundle.putString("NOVEL_TITLE", novelTitle);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void navigateToSearchPage() {
        Intent intent = new Intent(HomePage.this, SearchPage.class);
        startActivity(intent);
        finish();
    }

    private Novel responseToNovel(DocumentSnapshot snapshot) {
        return new Novel(snapshot.getId(), snapshot.getString("name"));
    }
}
