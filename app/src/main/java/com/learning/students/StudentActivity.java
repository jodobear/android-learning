package com.learning.students;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentActivity extends AppCompatActivity {
    public static final String NAME_EXTRA = "name";
    public static final String PHONE_EXTRA = "phone";
    public static final String EMAIL_EXTRA = "email";

    private ImageView photoView;
    private EditText fullNameEdit;
    private EditText phoneEdit;
    private EditText emailEdit;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        photoView = findViewById(R.id.photo);
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

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }
}
