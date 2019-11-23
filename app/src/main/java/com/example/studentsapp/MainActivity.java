package com.example.studentsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

class Student{
    private String fullName;
    private String phoneNum;
    private String email;
    private Bitmap photo;

    public Student(String fullName, String phoneNum, String email, Bitmap photo) {
        this.fullName = fullName;
        this.phoneNum = phoneNum;
        this.email = email;
        this.photo = photo;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int position = data.getIntExtra(POSITION_EXTRA, 0);
            students[position].setPhoto(image);
            studentsView.getAdapter().notifyDataSetChanged();

        }
        super.onActivityResult(requestCode,resultCode,data);
    }
    private static final int CAMERA_PIC_REQUEST = 3;
    private static final String POSITION_EXTRA = "position";

    class StudentViewHolder extends  RecyclerView.ViewHolder {
        private ImageView photoview;
        private TextView fullnameView;
        private TextView emailView;
        private TextView numberView;
        private Button takeAPhotoButton;
        private int position;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            photoview = itemView.findViewById(R.id.studentPhoto);
            fullnameView = itemView.findViewById(R.id.fullname);
            emailView = itemView.findViewById(R.id.email);
            numberView = itemView.findViewById(R.id.number);
            takeAPhotoButton = itemView.findViewById(R.id.takePhotoButton);
            takeAPhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(POSITION_EXTRA, position);
                    startActivityForResult(intent, CAMERA_PIC_REQUEST);
                }
            });
        }

        public void bind(int position) {
            this.position = position;
            Student student = students[position];
            fullnameView.setText(student.getFullName());
            emailView.setText(student.getEmail());
            numberView.setText(student.getPhoneNum());
            photoview.setImageBitmap(student.getPhoto());

        }
    }

    class StudentsAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.student, parent,false);
            return new StudentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((StudentViewHolder)holder).bind(position);
        }

        @Override
        public int getItemCount() {
            return students.length;
        }
    }


    private Student[] students = new Student[] {
            new Student("Arjun Nair", "+420 72 501 77 79", "Arjun@gmail.com", null),
            new Student("Jovan Velanac","+34 653 453 858", "Velanacjovan@gmail.com", null ),
            new Student("Leonardo Fanchini", "+34 653 06 36 47", "Leo@gmail.com", null)
    };

        private RecyclerView studentsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentsView = findViewById(R.id.studentsList);
        studentsView.setLayoutManager(new LinearLayoutManager(this));

        studentsView.setAdapter(new StudentsAdapter());
    }

}
