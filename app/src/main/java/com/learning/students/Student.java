package com.learning.students;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Student {
    private int id;
    private String fullName;
    private String phoneNumber;
    private String email;

    private Bitmap photo;

    public Student(int id, String fullName, String phoneNumber, String email, Bitmap photo) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.photo = photo;
    }
    public String getImageFileName() { return "image_" + id + ".png";}

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

    public static List<Student> generate() {
        List<Student> students = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            students.add(new Student(
                    i,
                    "student" + i,
                    "phone" + i,
                    "email" + i + "@com",
                    null

            ));
        }
        return students;
    }

    // storing students as string in csv format
    public static String convertToString(Student student){
        return student.id + "," +
                student.fullName + "," +
                student.phoneNumber + "," +
                student.email;
    }

    public static Student loadFromString(String text) {
        String[] data = text.split(",");
        return new Student(Integer.valueOf(data[0]), data[1], data[2], data[3], null);
    }

    public static void saveStudents(List<Student> students, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("");
            for (Student student : students) {
                writer.append(convertToString(student));
                writer.newLine();
            }
        }
        File photosDir = new File(file.getParentFile(), "photos");
        photosDir.mkdir();
        for(Student student: students) {
            student.saveImage(photosDir);
        }
    }

    public static List<Student> loadStudents(File file) throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (true) {
                String text = reader.readLine();
                if (text == null) {
                    break;
                }
                students.add(loadFromString(text));
            }
        }
        for (Student student: students) {
            student.loadImage(file.getParentFile());
        }
        return students;
    }

    public void saveImage(File photosDir) throws IOException {
        if (photo != null) {
            File imageFile = new File(photosDir, getImageFileName());
            imageFile.createNewFile();
            try(FileOutputStream stream = new FileOutputStream(imageFile)) {
                photo.compress(Bitmap.CompressFormat.PNG, 0, stream);
            }
        }
    }

    public void loadImage(File photosDir) throws IOException {
        File imageFile = new File(photosDir, getImageFileName());
        if (imageFile.exists()) {
            try (FileInputStream stream = new FileInputStream(imageFile)) {
                photo = BitmapFactory.decodeStream(stream);
            }
        }
    }
}
//TODO: 1. Fix identifier for students in MainActivity
// 2. Store File in an accessible Dir. Will require permission to write  & read.