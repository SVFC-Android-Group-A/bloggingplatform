package com.example.bloggingplatformgroupa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button createPostButton, logoutButton;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createPostButton = findViewById(R.id.createPostButton);
        logoutButton = findViewById(R.id.logoutButton);
        recyclerView = findViewById(R.id.recyclerView);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter();
        recyclerView.setAdapter(postAdapter);

        // Load posts
        loadPosts();

        createPostButton.setOnClickListener(v -> navigateToCreatePost());
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void loadPosts() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot documentSnapshots = task.getResult();
                        List<Post> posts = documentSnapshots.toObjects(Post.class);
                        postAdapter.setPosts(posts);
                    } else {
                        Toast.makeText(HomeActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToCreatePost() {
        startActivity(new Intent(HomeActivity.this, CreatePostActivity.class));
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}
