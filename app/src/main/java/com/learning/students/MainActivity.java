package com.learning.students;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PIC_REQUEST = 3;
    private static final int ADD_STUDENT_REQUEST = 2;
    private static final String CURRENT_POSITION_EXTRA = "current_position";

    private static int currentStudentPosition = 0;

    private List<Student> students = new ArrayList<>();

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private ImageView photoView;
        private TextView fullNameView;
        private TextView emailView;
        private TextView phoneView;
        private Button takePhotoButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo);
            fullNameView = itemView.findViewById(R.id.fullName);
            emailView = itemView.findViewById(R.id.email);
            phoneView = itemView.findViewById(R.id.phone);
            takePhotoButton = itemView.findViewById(R.id.takePhoto);
            takePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.currentStudentPosition = position;
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                }
            });
        }

        public void bind(int position, Student student) {
            this.position = position;
            fullNameView.setText(student.getFullName());
            emailView.setText(student.getEmail());
            phoneView.setText(student.getPhoneNumber());
            photoView.setImageBitmap(student.getPhoto());
        }
    }

    class StudentsAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.student, parent, false);
            return new StudentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((StudentViewHolder) holder).bind(position, students.get(position));
        }

        @Override
        public int getItemCount() {
            return students.size();
        }
    }

    private RecyclerView studentsListView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            students.get(currentStudentPosition).setPhoto(image);
            studentsListView.getAdapter().notifyDataSetChanged();
        } else if (requestCode == ADD_STUDENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra(StudentActivity.NAME_EXTRA);
                String phone = data.getStringExtra(StudentActivity.PHONE_EXTRA);
                String email = data.getStringExtra(StudentActivity.EMAIL_EXTRA);
                Student student = new Student(name, phone, email, null);
                students.add(student);
                studentsListView.getAdapter().notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentsListView = findViewById(R.id.studentsListView);
        studentsListView.setLayoutManager(new LinearLayoutManager(this));

        studentsListView.setAdapter(new StudentsAdapter());

        findViewById(R.id.addStudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(MainActivity.this, StudentActivity.class),
                        ADD_STUDENT_REQUEST
                );
            }
        });

        if (savedInstanceState != null) {
            currentStudentPosition = savedInstanceState.getInt(CURRENT_POSITION_EXTRA, 0);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION_EXTRA, currentStudentPosition);
    }
}
