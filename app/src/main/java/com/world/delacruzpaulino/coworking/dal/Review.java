package com.world.delacruzpaulino.coworking.dal;

/**
 * Created by delacruzpaulino on 5/14/16.
 */
public class Review {


    int stars;
    String comment;
    String userId;

    public Review() {
    }

    public Review(int stars, String comment, String userId) {
        this.stars = stars;
        this.comment = comment;
        this.userId = userId;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
