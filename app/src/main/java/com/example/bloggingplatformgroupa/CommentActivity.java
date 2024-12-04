package com.example.bloggingplatformgroupa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private EditText commentEditText;
    private Button postCommentButton;
    private FirebaseFirestore db;
    private String userId; // Store your authenticated user's ID here
    private String postId; // Get the postId where the comment should be added

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Initialize views
        commentEditText = findViewById(R.id.commentEditText);
        postCommentButton = findViewById(R.id.postCommentButton);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get the authenticated user's ID
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the postId to which the comment should be added (this could come from the intent or another method)
        postId = getIntent().getStringExtra("POST_ID");

        // Set onClickListener for the button
        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });
    }

    private void addComment() {
        String commentContent = commentEditText.getText().toString();

        if (commentContent.isEmpty()) {
            Toast.makeText(CommentActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new comment map
        Map<String, Object> comment = new HashMap<>();
        comment.put("userId", userId);
        comment.put("content", commentContent);
        comment.put("timestamp", FieldValue.serverTimestamp());

        // Reference to the post document
        DocumentReference postRef = db.collection("posts").document(postId);

        // Add the comment to the 'comments' subcollection under the post
        postRef.collection("comments")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CommentActivity.this, "Comment added successfully!", Toast.LENGTH_SHORT).show();
                        commentEditText.setText(""); // Optionally clear the input field
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(CommentActivity.this, "Error adding comment.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
