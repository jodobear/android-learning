package com.learning.students;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int CAMERA_PIC_REQUEST = 3; // requestCode for camera activity - picked at random
    private static final int ADD_STUDENT_REQUEST = 2;
    private static final String CURRENT_POSITION_EXTRA = "current_position";

    private static int currentStudentPosition = 0;

    private List<Student> students = new ArrayList<>(); // List<dataType>

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView group;

        public GroupViewHolder (@NonNull View itemView) {
            super(itemView);
            group = itemView.findViewById(R.id.group);
        }

        public void bind (int index) {
            group.setText(getString(R.string.group, index));
        }
    }

    class StudentViewHolder extends RecyclerView.ViewHolder {
        private int position;
        private ImageView photoView;
        private TextView fullNameView;
        private TextView emailView;
        private TextView phoneView;
        private Button removeStudent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            photoView = itemView.findViewById(R.id.photo);
            fullNameView = itemView.findViewById(R.id.fullName);
            emailView = itemView.findViewById(R.id.email);
            phoneView = itemView.findViewById(R.id.phone);
            removeStudent = itemView.findViewById(R.id.removeStudentButton);

            removeStudent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

        private static final int STUDENT_VIEW_TYPE = 0;
        private static final int GROUP_VIEW_TYPE = 1;
        private int index = 0;

        /**
         * Return the view type of the item at <code>position</code> for the purposes
         * of view recycling.
         *
         * <p>The default implementation of this method returns 0, making the assumption of
         * a single view type for the adapter. Unlike ListView adapters, types need not
         * be contiguous. Consider using id resources to uniquely identify item view types.
         *
         * @param position position to query
         * @return integer value identifying the type of the view needed to represent the item at
         * <code>position</code>. Type codes need not be contiguous.
         */
        @Override
        public int getItemViewType(int position) {
            if (position % 11 == 0) {
                return GROUP_VIEW_TYPE;
            } else {
                return STUDENT_VIEW_TYPE;
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            if (viewType == GROUP_VIEW_TYPE) {
                View view = layoutInflater.inflate(R.layout.group, parent, false);
                return new GroupViewHolder(view);
            }
            View view = layoutInflater.inflate(R.layout.student, parent, false);
            return new StudentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof StudentViewHolder) {
                position = position - (position / 11) - 1;
                ((StudentViewHolder) holder).bind(position, students.get(position));
            }
            if (holder instanceof GroupViewHolder) {
                index = 1 + position / 11;
                ((GroupViewHolder) holder).bind(index);
            }
        }

        @Override
        public int getItemCount() { return students.size() + (students.size() / 10); }
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
                String email = data.getStringExtra(StudentActivity.EMAIL_EXTRA);
                String phone = data.getStringExtra(StudentActivity.PHONE_EXTRA);
                Bitmap image = data.getParcelableExtra(StudentActivity.IMAGE_EXTRA);
                Student student = new Student(0, name, email, phone, image); //TODO: fix identifier
                students.add(student);
                studentsListView.getAdapter().notifyDataSetChanged();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Button addStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentsListView = findViewById(R.id.studentsListView);
        studentsListView.setLayoutManager(new LinearLayoutManager(this));

        File studentsFile = new File(getFilesDir(), "students.txt");
        try {
            if (!studentsFile.exists()) {
                students = Student.generate();
                studentsFile.createNewFile();
                Student.saveStudents(students, studentsFile);
            } else {
                students = Student.loadStudents(studentsFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        studentsListView.setAdapter(new StudentsAdapter());

        if (savedInstanceState != null) {
            currentStudentPosition = savedInstanceState.getInt(CURRENT_POSITION_EXTRA, 0);
        }

        addStudent = findViewById(R.id.addStudent);

        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addStudent_activity = new Intent(MainActivity.this, StudentActivity.class);
                startActivityForResult(addStudent_activity, ADD_STUDENT_REQUEST);
            }
        });

    }

    // This method saves the current position if the activity is destroyed which we can restore on activity restore.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt(CURRENT_POSITION_EXTRA, currentStudentPosition);
    }
}
