package com.example.bloggingplatformgroupa;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FirestoreHelper {

    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void addComment(String postId, Comment comment) {
        db.collection("posts").document(postId).collection("comments")
                .add(comment)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Comment added successfully!");
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error adding comment: " + e.getMessage());
                });
    }

    public void getComments(String postId) {
        db.collection("posts").document(postId)
                .collection("comments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {  // Use QueryDocumentSnapshot instead of DocumentSnapshot
                        Comment comment = document.toObject(Comment.class);
                        System.out.println("Comment: " + comment.getCommentText() + " by " + comment.getCommenterName());
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println("Error retrieving comments: " + e.getMessage());
                });
    }
}
