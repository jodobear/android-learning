package com.learning.students;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private Button registerButton;
    private Button loginButton;

    private EditText loginEdit;
    private EditText passwordEdit;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String hash = getHash(password);
                user.setUsername(username);
                user.setPassword(hash);
                storeIntoPreferences(user);
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String hash = getHash(password);
                if (username.equals(user.getUsername())) {
                    if (hash.equals(user.getPassword())) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, R.string.wrong_login, Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginEdit = findViewById(R.id.loginEdit);
        passwordEdit = findViewById(R.id.passwordEdit);

        user = loadFromPreferences();
    }

    private String getHash(String password) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private User loadFromPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");
        return new User(username, password);
    }

    private void storeIntoPreferences(User user) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, user.getUsername());
        editor.putString(PASSWORD, user.getPassword());
        editor.apply();
    }
}
