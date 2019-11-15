package com.learning.students;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.learning.students.MainActivity.CAMERA_PIC_REQUEST;

public class StudentActivity extends AppCompatActivity {
    public static final String NAME_EXTRA = "name";
    public static final String PHONE_EXTRA = "phone";
    public static final String EMAIL_EXTRA = "email";
    public static final String IMAGE_EXTRA = "image";

    private ImageView photoView;
    private EditText fullNameEdit;
    private EditText phoneEdit;
    private EditText emailEdit;
    private Button confirmButton;

    private Bitmap studentPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        photoView = findViewById(R.id.photo);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_PIC_REQUEST);
            }
        });
        fullNameEdit = findViewById(R.id.fullNameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        emailEdit = findViewById(R.id.emailEdit);
        confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullNameEdit.getText().toString();
                String phone = phoneEdit.getText().toString();
                String email = emailEdit.getText().toString();

                Intent data = new Intent();
                data.putExtra(NAME_EXTRA, name);
                data.putExtra(PHONE_EXTRA, phone);
                data.putExtra(EMAIL_EXTRA, email);
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
            photoView.setImageBitmap(studentPhoto);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
