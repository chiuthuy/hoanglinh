package com.dev.novel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.novel.Adapters.ChapterAdapter;
import com.dev.novel.Models.Chapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NovelPage extends AppCompatActivity {
    private int chaptersCount;
    private FirebaseFirestore db;
    private Button homePageButton;
    private ListView listOfChapters;
    private String novelId, novelTitleString;
    private TextView novelTitle, novelTitleTop;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.novel_page);
        initState();
        setNovelTitle();
        getAllChapters();
        homePageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomePage();
            }
        });
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        chaptersCount = 0;
        initBundleState();
        initWidgetState();
    }

    private void initBundleState() {
        novelId = getIntent().getExtras().getString("NOVEL_ID");
        novelTitleString = getIntent().getExtras().getString("NOVEL_TITLE");
    }

    private void initWidgetState() {
        novelTitle = (TextView) findViewById(R.id.novelTitle);
        novelTitleTop = (TextView) findViewById(R.id.novelTitleTop);
        homePageButton = (Button) findViewById(R.id.homePageButton);
        listOfChapters = (ListView) findViewById(R.id.listOfChapters);
    }

    private void setNovelTitle() {
        novelTitle.setText(novelTitleString);
        novelTitleTop.setText(novelTitleString);
    }

    private void getAllChapters() {
        CollectionReference chapters = db.collection(novelId);
        Query query = chapters.orderBy("id");
        getChapters(query);
    }

    private void getChapters(Query query) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Chapter> listOfChapters = new ArrayList<>();
                    Chapter[] chapters = {};

                    chaptersCount = 0;

                    for (DocumentSnapshot chapter: task.getResult()) {
                        listOfChapters.add(responseToChapter(chapter));
                        ++ chaptersCount;
                    }

                    chapters = listOfChapters.toArray(chapters);
                    renderChapters(chapters);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NovelPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderChapters(final Chapter[] chapters) {
        ChapterAdapter adapter = new ChapterAdapter(NovelPage.this, chapters);
        listOfChapters.setAdapter(adapter);
        listOfChapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateToChapterPage(position);
            }
        });
    }

    private void navigateToHomePage() {
        Intent intent = new Intent(NovelPage.this, HomePage.class);
        startActivity(intent);
        finish();
    }

    private void navigateToChapterPage(int position) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(NovelPage.this, ChapterPage.class);

        bundle.putString("NOVEL_ID", novelId);
        bundle.putInt("CHAPTER_POSITION", position);
        bundle.putInt("CHAPTERS_COUNT", chaptersCount);
        bundle.putString("NOVEL_TITLE", novelTitleString);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private Chapter responseToChapter(DocumentSnapshot response) {
        return new Chapter(response.getId(), response.getString("title"), "");
    }
}
