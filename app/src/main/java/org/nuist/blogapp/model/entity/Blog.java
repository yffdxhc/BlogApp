package org.nuist.blogapp.model.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Blog implements Serializable {
    private String blog_id;
    private String blog_title;
    private String blog_content;
    private String blog_summary;
    private String user_number;
    private String username;
    private String user_avatar;
    private Integer type_id;
    private Integer blog_status;
    private Timestamp create_time;
    private Timestamp update_time;
    private String cover_image;

    public Blog() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getBlog_title() {
        return blog_title;
    }

    public void setBlog_title(String blog_title) {
        this.blog_title = blog_title;
    }

    public String getBlog_content() {
        return blog_content;
    }

    public void setBlog_content(String blog_content) {
        this.blog_content = blog_content;
    }

    public String getBlog_summary() {
        return blog_summary;
    }

    public void setBlog_summary(String blog_summary) {
        this.blog_summary = blog_summary;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public Integer getBlog_status() {
        return blog_status;
    }

    public void setBlog_status(Integer blog_status) {
        this.blog_status = blog_status;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id='" + blog_id + '\'' +
                ", blog_title='" + blog_title + '\'' +
                ", blog_content='" + blog_content + '\'' +
                ", blog_summary='" + blog_summary + '\'' +
                ", user_number='" + user_number + '\'' +
                ", username='" + username + '\'' +
                ", user_avatar='" + user_avatar + '\'' +
                ", type_id=" + type_id +
                ", blog_status=" + blog_status +
                ", create_time=" + create_time +
                ", update_time=" + update_time +
                ", cover_image='" + cover_image + '\'' +
                '}';
    }
}
