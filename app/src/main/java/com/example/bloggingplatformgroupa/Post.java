package com.example.bloggingplatformgroupa;

public class Post {

    private String title;
    private String content;
    private long likes;

    public Post() {
        // Default constructor for Firestore
    }

    public Post(String title, String content, long likes) {
        this.title = title;
        this.content = content;
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }
}
