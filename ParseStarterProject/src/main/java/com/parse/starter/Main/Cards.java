package com.parse.starter.Main;




public class Cards {
    private String userId;
    private String cardId;
    private String name, profileImageUrl, bio, interest;
    private String age;
    private int distance;
    private Boolean like;


    public Cards(String userId,String cardId, String name, String age, String profileImageUrl, String bio, String interest, int distance, Boolean like) {
        this.userId = userId;
        this.cardId = cardId;
        this.name = name;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.interest = interest;
        this.distance = distance;
        this.like = like;
    }

    public Cards(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getDistance() {
        return distance;
    }

    public Boolean getLikeStatus() {
        return like;
    }

    public void setLikeStatus(Boolean bool) {
        this.like = bool;
    }

    public String getBio() {
        return bio;
    }

    public String getInterest() {
        return interest;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
