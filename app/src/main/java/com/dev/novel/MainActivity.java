package com.dev.novel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dev.novel.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private EditText usernameInput, passwordInput;
    private Button logInButton, registerButton;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (isLoggedIn()) {
            navigate(MainActivity.this, HomePage.class);
        } else {
            setContentView(R.layout.login_page);
            initState();

            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateInput()) {
                        logIn(userFromInput());
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    navigate(MainActivity.this, RegisterPage.class);
                }
            });
        }
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        logInButton = (Button) findViewById(R.id.logInButton);
        registerButton = (Button) findViewById(R.id.registerButton);
    }

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERNAME", MODE_PRIVATE);
        return sharedPreferences.getString("username", null) != null;
    }

    private boolean validateInput() {
        boolean username = !usernameInput.getText().toString().equals("");
        boolean password = !passwordInput.getText().toString().equals("");

        return username && password;
    }

    private User userFromInput() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        return new User(username, password);
    }

    private void logIn(final User user) {
        CollectionReference users = db.collection("users");
        Query userQuery = users.whereEqualTo("username", user.getUsername());

        userQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Username is not exist", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentSnapshot userSnapshot = documentSnapshots.getDocuments().get(0);
                    String password = userSnapshot.getString("password");

                    if (user.getPassword().compareTo(password) == 0) {
                        saveLogInInfo();
                        navigate(MainActivity.this, HomePage.class);
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveLogInInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERNAME", MODE_PRIVATE);
        String username = usernameInput.getText().toString();
        sharedPreferences.edit().putString("username", username).apply();
    }

    private void navigate(Context context, Class page) {
        Intent intent = new Intent(context, page);
        startActivity(intent);
        finish();
    }
}
