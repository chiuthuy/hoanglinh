package com.dev.novel;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.novel.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class RegisterPage extends AppCompatActivity {
    private FirebaseFirestore db;
    private Button logInButton, registerButton;
    private EditText usernameInput, passwordInput, rePasswordInput;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.register_page);
        initState();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigate(RegisterPage.this, MainActivity.class);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void initState() {
        db = FirebaseFirestore.getInstance();
        logInButton = (Button) findViewById(R.id.logInButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        rePasswordInput = (EditText) findViewById(R.id.rePasswordInput);
    }

    private User userFromInput() {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        return new User(username, password);
    }

    private boolean validateInput() {
        String password = passwordInput.getText().toString();
        String rePassword = rePasswordInput.getText().toString();
        boolean usernameValid = !usernameInput.getText().toString().isEmpty();
        boolean passwordValid = password.compareTo(rePassword) == 0;

        return usernameValid && passwordValid;
    }

    private void checkDuplicate(final User user) {
        CollectionReference users = db.collection("users");
        Query userQuery = users.whereEqualTo("username", user.getUsername());

        userQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (documentSnapshots.isEmpty()) {
                    createUser(user);
                } else {
                    Toast.makeText(RegisterPage.this, "User is already exist", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createUser(User user) {
        CollectionReference users = db.collection("users");

        users.add(user.toMap()).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(RegisterPage.this, "Register success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPage.this, "Register failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register() {
        if (validateInput()) {
            checkDuplicate(userFromInput());
        } else {
            Toast.makeText(RegisterPage.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigate(Context context, Class page) {
        Intent intent = new Intent(context, page);
        startActivity(intent);
        finish();
    }
}
