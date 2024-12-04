package com.example.bloggingplatformgroupa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class PostDetailActivity extends AppCompatActivity {

    private TextView postTitleTextView, postContentTextView, likeCountTextView;
    private ImageView likeImageView;
    private Button commentButton;
    private FirebaseFirestore db;
    private String postId;
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize views
        postTitleTextView = findViewById(R.id.postTitleTextView);
        postContentTextView = findViewById(R.id.postContentTextView);
        likeCountTextView = findViewById(R.id.likeCountTextView);
        likeImageView = findViewById(R.id.likeImageView);
        commentButton = findViewById(R.id.commentButton);

        db = FirebaseFirestore.getInstance();

        // Get postId passed from HomeActivity
        postId = getIntent().getStringExtra("POST_ID");

        // Load post data
        loadPostData();

        // Set onClickListeners
        likeImageView.setOnClickListener(v -> toggleLike());
        commentButton.setOnClickListener(v -> navigateToCommentActivity());
    }

    private void loadPostData() {
        DocumentReference postRef = db.collection("posts").document(postId);
        postRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String title = documentSnapshot.getString("title");
                String content = documentSnapshot.getString("content");
                long likes = documentSnapshot.getLong("likes");

                // Set the post data on UI elements
                postTitleTextView.setText(title);
                postContentTextView.setText(content);
                likeCountTextView.setText(String.valueOf(likes));

                // Update the like button image based on the 'isLiked' flag
                likeImageView.setImageResource(isLiked ? R.drawable.ic_liked : R.drawable.ic_unliked);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(PostDetailActivity.this, "Error loading post data", Toast.LENGTH_SHORT).show();
        });
    }

    private void toggleLike() {
        DocumentReference postRef = db.collection("posts").document(postId);

        // Update the 'likes' field in Firestore based on the current state of isLiked
        postRef.update("likes", isLiked ? FieldValue.increment(-1) : FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    isLiked = !isLiked;
                    likeImageView.setImageResource(isLiked ? R.drawable.ic_liked : R.drawable.ic_unliked);
                    loadPostData(); // Reload the post data to update the like count
                    Toast.makeText(PostDetailActivity.this, isLiked ? "Liked" : "Unliked", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(PostDetailActivity.this, "Error updating like", Toast.LENGTH_SHORT).show());
    }

    private void navigateToCommentActivity() {
        // Ensure CommentActivity is imported at the top of the file
        Intent intent = new Intent(PostDetailActivity.this, CommentActivity.class);
        intent.putExtra("POST_ID", postId);
        startActivity(intent);
    }
}
