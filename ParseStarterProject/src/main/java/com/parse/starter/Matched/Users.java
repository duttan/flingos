package com.parse.starter.Matched;


public class Users {
    private String userId;
    private String cardId;
    private String name, profileImageUrl, bio, interest;
    private String age;
    private int distance;

    public Users(String userId,String cardId, String name, String age, String profileImageUrl, String bio, String interest, int distance) {
        this.userId = userId;
        this.cardId = cardId;
        this.name = name;
        this.age = age;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;
        this.interest = interest;
        this.distance = distance;
    }

    public Users(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public int getDistance() {
        return distance;
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
