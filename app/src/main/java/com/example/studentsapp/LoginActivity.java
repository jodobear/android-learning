package com.example.studentsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

class User {
    public void setUsername(String username) {

        this.username = username;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    private String username;
    private String password;

    public User(String username, String password){
        this.username = username;
        this. password = password;
    }

    public String getUsername() {

        return username;
    }

    public String getPassword() {

        return password;
    }
}

public class LoginActivity extends AppCompatActivity {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    private Button registerButton;
    private Button loginButton;
    private EditText loginEdit;
    private EditText passEdit;
    private Button resetButton;
    private Boolean isTherePassword = false;
    private EditText changePassEdit;
    private Button changePassButton;
    private Button registerPassButton;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginEdit.getText().toString();
                String password = passEdit.getText().toString();
                if(username.equals("")){
                    Toast.makeText(LoginActivity.this , R.string.usernamempty,Toast.LENGTH_SHORT).show();
            } else if(!isTherePassword || password.equals(user.getPassword())){
                    user.setUsername(username);
                    user.setPassword(password);
                    storeIntoPreferences(user);
                    isTherePassword = true;
                    Toast.makeText(LoginActivity.this , R.string.successreg,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this , R.string.unsuccessreg,Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginButton = findViewById(R.id.loginButton);
        loginEdit = findViewById(R.id.login);
        passEdit = findViewById(R.id.password);
        changePassEdit = findViewById(R.id.changePasswordEdit);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginEdit.getText().toString();
                String password = passEdit.getText().toString();
                if (username.equals(user.getUsername()) && user.getUsername() != ""){
                    if (password.equals(user.getPassword())){
                        Intent gotoUser = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(gotoUser);
                    }else{
                        Toast.makeText(LoginActivity.this , R.string.wrong_password,Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this , R.string.wrong_login,Toast.LENGTH_SHORT).show();
                }
            }
        });
        user = loadFromPreferences();

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername("");
                user.setPassword("");
                storeIntoPreferences(user);
                isTherePassword = false;
                registerPassButton.setVisibility(View.GONE);
                changePassEdit.setVisibility(View.GONE);
            }
        });

        changePassButton = findViewById(R.id.changePasswordButton);
        changePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = loginEdit.getText().toString();
                String password = passEdit.getText().toString();
                if (username.equals(user.getUsername()) && user.getUsername() != ""){
                    if (password.equals(user.getPassword())) {
                        changePassEdit.setVisibility(View.VISIBLE);
                        registerPassButton.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(LoginActivity.this , R.string.wrong_password,Toast.LENGTH_SHORT).show();
                    }
                    }else{
                    Toast.makeText(LoginActivity.this , R.string.wrong_login,Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerPassButton = findViewById(R.id.registerPasswordButton);
        registerPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = changePassEdit.getText().toString();
                if(password.equals("")){
                    Toast.makeText(LoginActivity.this , R.string.passempty,Toast.LENGTH_SHORT).show();
                } else{
                    user.setPassword(password);
                    storeIntoPreferences(user);
                    registerPassButton.setVisibility(View.GONE);
                    changePassEdit.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this , R.string.success,Toast.LENGTH_SHORT).show();
                    changePassEdit.getText().clear();
                }
            }
        });

        registerPassButton.setVisibility(View.GONE);
        changePassEdit.setVisibility(View.GONE);

    }


    private User loadFromPreferences() {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        String username = preferences.getString(USERNAME, "");
        String password = preferences.getString(PASSWORD, "");
        return new User(username, password);


    }
    private void storeIntoPreferences(User user){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERNAME, user.getUsername());
        editor.putString(PASSWORD, user.getPassword());
        editor.apply();
    }
}
