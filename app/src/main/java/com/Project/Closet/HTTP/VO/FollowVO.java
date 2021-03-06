package com.Project.Closet.HTTP.VO;

public class FollowVO {
    private String followerID, followedID, regDate;

    public FollowVO(String followerID, String followedID){
        this.followerID = followerID;
        this.followedID = followedID;
    }

    public String getFollowerID() {
        return followerID;
    }

    public void setFollowerID(String followerID) {
        this.followerID = followerID;
    }

    public String getFollowedID() {
        return followedID;
    }

    public void setFollowedID(String followedID) {
        this.followedID = followedID;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
