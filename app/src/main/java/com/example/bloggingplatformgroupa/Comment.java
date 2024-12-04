package com.example.bloggingplatformgroupa;

public class Comment {

    private String commentText;
    private String commenterName;

    // Constructor
    public Comment(String commentText, String commenterName) {
        this.commentText = commentText;
        this.commenterName = commenterName;
    }

    // Getter and Setter methods
    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }
}
