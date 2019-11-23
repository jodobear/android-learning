package com.learning.students;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import static com.learning.students.MainActivity.CAMERA_PIC_REQUEST;

public class StudentActivity extends AppCompatActivity {
    public static final String NAME_EXTRA = "name";
    public static final String EMAIL_EXTRA = "email";
    public static final String PHONE_EXTRA = "phone";
    public static final String IMAGE_EXTRA = "byteArray";

    private ImageView photoViewV;
    private EditText fullNameEditV;
    private EditText emailEditV;
    private EditText phoneEditV;
    private Button confirmButtonV;

    private Bitmap studentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        photoViewV = findViewById(R.id.addStudentPhoto);
        photoViewV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });
        fullNameEditV = findViewById(R.id.fullNameEdit);
        emailEditV = findViewById(R.id.emailEdit);
        phoneEditV = findViewById(R.id.phoneEdit);
        confirmButtonV = findViewById(R.id.confirm);

        confirmButtonV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullNameEditV.getText().toString();
                String email = emailEditV.getText().toString();
                String phone = phoneEditV.getText().toString();

                Intent data = new Intent();
                data.putExtra(NAME_EXTRA, name);
                data.putExtra(EMAIL_EXTRA, email);
                data.putExtra(PHONE_EXTRA, phone);
                data.putExtra(IMAGE_EXTRA, studentPhoto);

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                studentPhoto = (Bitmap) data.getExtras().get("data");
                photoViewV.setImageBitmap(studentPhoto);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
}
