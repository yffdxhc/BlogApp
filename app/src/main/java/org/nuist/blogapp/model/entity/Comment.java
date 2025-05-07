package org.nuist.blogapp.model.entity;

public class Comment {
    private String avatarUrl;
    private String nickname;
    private String content;

    public Comment(String avatarUrl, String nickname, String content) {
        this.avatarUrl = avatarUrl;
        this.nickname = nickname;
        this.content = content;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }
}
