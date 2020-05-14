package com.dev.novel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ChapterPage extends AppCompatActivity {
    private FirebaseFirestore db;
    private int chapterPosition, chaptersCount;
    private TextView chapterTitle, chapterContent;
    private String novelId, novelTitle, chapterTitleString;
    private Button novelPageButton, previousButton, nextButton, topPreviousButton, topNextButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.chapter_page);
        initState();
        getChapterContent(chapterPosition);
        setNextButtonListener();
        setPreviousButtonListener();

        novelPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToNovelPage(novelId, novelTitle);
            }
        });
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        initWidgetData();
        initBundleData();
    }

    private void initWidgetData() {
        chapterTitle = (TextView) findViewById(R.id.chapterTitle);
        chapterContent = (TextView) findViewById(R.id.chapterContent);
        novelPageButton = (Button) findViewById(R.id.novelPageButton);
        topNextButton = (Button) findViewById(R.id.topNextButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        topPreviousButton = (Button) findViewById(R.id.topPreviousButton);
        previousButton = (Button) findViewById(R.id.previousButton);
    }

    private void initBundleData() {
        novelId = getIntent().getExtras().getString("NOVEL_ID");
        novelTitle = getIntent().getExtras().getString("NOVEL_TITLE");
        chaptersCount = getIntent().getExtras().getInt("CHAPTERS_COUNT");
        chapterPosition = getIntent().getExtras().getInt("CHAPTER_POSITION");
    }

    private void setNovelTitle() {
        String titleString = "Chương " + Integer.toString(chapterPosition + 1);
        chapterTitle.setText(titleString + ": " + chapterTitleString);
    }

    private void setChapterContent(String content) {
        chapterContent.setText(content);
    }

    private void getChapterContent(int chapterPosition) {
        db.collection(novelId).whereEqualTo("id", Integer.toString(chapterPosition)).get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    String content = "Không có nội dung";
                    for (QueryDocumentSnapshot chapter: task.getResult()) {
                        content = chapter.getString("content");
                        chapterTitleString = chapter.getString("title");
                    }

                    setNovelTitle();
                    setChapterContent(content);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChapterPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNextButtonListener() {
        if (chapterPosition < chaptersCount - 1) {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChapterPage.this, ChapterPage.class);

                    intent.putExtras(chapterPageBundle(chapterPosition + 1));
                    startActivity(intent);
                    finish();
                }
            });

            topNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChapterPage.this, ChapterPage.class);

                    intent.putExtras(chapterPageBundle(chapterPosition + 1));
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            nextButton.setBackgroundColor(nextButton.getContext().getResources().getColor(
                    R.color.common_google_signin_btn_text_light_disabled,
                    getTheme()
            ));

            topNextButton.setBackgroundColor(topNextButton.getContext().getResources().getColor(
                    R.color.common_google_signin_btn_text_light_disabled,
                    getTheme()
            ));
        }
    }

    private void setPreviousButtonListener() {
        if (chapterPosition > 0) {
            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChapterPage.this, ChapterPage.class);

                    intent.putExtras(chapterPageBundle(chapterPosition - 1));
                    startActivity(intent);
                    finish();
                }
            });

            topPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ChapterPage.this, ChapterPage.class);

                    intent.putExtras(chapterPageBundle(chapterPosition - 1));
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            previousButton.setBackgroundColor(previousButton.getContext().getResources().getColor(
                    R.color.common_google_signin_btn_text_light_disabled,
                    getTheme()
            ));

            topPreviousButton.setBackgroundColor(topPreviousButton.getContext().getResources().getColor(
                    R.color.common_google_signin_btn_text_light_disabled,
                    getTheme()
            ));
        }
    }

    private void navigateToNovelPage(String novelId, String novelTitle) {
        Intent intent = new Intent(ChapterPage.this, NovelPage.class);

        intent.putExtras(novelPageBundle());
        startActivity(intent);
        finish();
    }

    private Bundle novelPageBundle() {
        Bundle bundle = new Bundle();

        bundle.putString("NOVEL_ID", novelId);
        bundle.putString("NOVEL_TITLE", novelTitle);

        return bundle;
    }

    private Bundle chapterPageBundle(int position) {
        Bundle bundle = new Bundle();

        bundle.putString("NOVEL_ID", novelId);
        bundle.putInt("CHAPTER_POSITION", position);
        bundle.putInt("CHAPTERS_COUNT", chaptersCount);
        bundle.putString("NOVEL_TITLE", novelTitle);

        return bundle;
    }
}
