package com.example.basicapplication.adapter;

public class Subject {
    String subject;
    int imgResource;

    public Subject(String subject, int imgResource) {
        this.subject = subject;
        this.imgResource = imgResource;
    }

    public Subject() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
    }
}
