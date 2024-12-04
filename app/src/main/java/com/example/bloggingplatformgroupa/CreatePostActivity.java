package com.example.bloggingplatformgroupa;

// CreatePostActivity.java

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePostActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private Button postButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        postButton = findViewById(R.id.postButton);
        db = FirebaseFirestore.getInstance();

        postButton.setOnClickListener(v -> createPost());
    }

    private void createPost() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new post with initial likes set to 0
        Post post = new Post(title, content, 0);

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Post created successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to the previous screen (HomeActivity)
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error creating post", Toast.LENGTH_SHORT).show());
    }
}
