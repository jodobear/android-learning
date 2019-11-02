package com.learning.students;

import android.graphics.Bitmap;

class Student {
    private String fullName;
    private String phoneNumber;
    private String email;

    private Bitmap photo;

    public Student(String fullName, String phoneNumber, String email, Bitmap photo) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photo = photo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
