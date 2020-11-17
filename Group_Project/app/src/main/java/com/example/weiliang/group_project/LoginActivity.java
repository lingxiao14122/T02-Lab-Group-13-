package com.example.weiliang.group_project;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public final static String KEY_USERNAME = "com.example.weiliang.group_project.Username";
    public final static String KEY_FULLNAME = "com.example.weiliang.group_project.FullName";
    public final static String KEY_PASSWORD = "com.example.weiliang.group_project.Password";
    public final static int REGISTER_REQUEST_CODE = 1;

    private EditText mEt_username;
    private EditText mEt_password;

    private DatabaseOpenHelper mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEt_username = findViewById(R.id.et_username_login);
        mEt_password = findViewById(R.id.et_password_login);

        mDb = new DatabaseOpenHelper(this);
        mDb.getReadableDatabase();
    }

    public void btnRegister_clicked(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    public void btnLogin_clicked(View view) {
        boolean checkEditText = checkAllEditText();

        if(checkEditText){
            String username = mEt_username.getText().toString(), password = mEt_password.getText().toString();

            boolean checkAuth = mDb.checkAuth(username, password);

            if(checkAuth){
                Toast.makeText(LoginActivity.this, "Successful Login.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Login failed, Username or password is incorrect.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Error: Input not completed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkAllEditText(){
        boolean result = false, usernameResult = false, passwordResult = false;

        if(TextUtils.isEmpty(mEt_username.getText().toString())){
            mEt_username.setError("This item \"Username\" cannot be empty.");
            usernameResult = false;
        } else {
            usernameResult = true;
        }
        if(TextUtils.isEmpty(mEt_password.getText().toString())){
            mEt_password.setError("This item \"Password\" cannot be empty.");
            passwordResult = false;
        } else {
            passwordResult = true;
        }
        if(usernameResult && passwordResult){
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK){
            String username = data.getStringExtra(KEY_USERNAME);
            String fullName = data.getStringExtra(KEY_FULLNAME);
            String password = data.getStringExtra(KEY_PASSWORD);

            mDb.insertUserInfo(fullName, username, password);
            mEt_username.setText(username);
        }
    }
}
