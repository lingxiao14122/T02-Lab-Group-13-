package com.example.weiliang.group_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText mEt_fullName;
    private EditText mEt_username;
    private EditText mEt_password;
    private EditText mEt_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEt_fullName = findViewById(R.id.et_fullName);
        mEt_username = findViewById(R.id.et_username_register);
        mEt_password = findViewById(R.id.et_password_register);
        mEt_password2 = findViewById(R.id.et_password2);
    }

    public void btnRegister_clicked(View view) {
        boolean checkEditText = checkAllEditText(), checkPassword;

        if(checkEditText){
            checkPassword = checkPassword();
            if(checkPassword){
                String fullName = mEt_fullName.getText().toString(), username = mEt_username.getText().toString(), password = mEt_password.getText().toString();

                Intent reply = new Intent();
                reply.putExtra(LoginActivity.KEY_USERNAME, username);
                reply.putExtra(LoginActivity.KEY_FULLNAME, fullName);
                reply.putExtra(LoginActivity.KEY_PASSWORD, password);
                setResult(RESULT_OK, reply);
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Error: Password not same.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "Error: Input not completed", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCancel_clicked(View view){
        Intent reply = new Intent();
        setResult(RESULT_CANCELED, reply);
        finish();
    }

    public boolean checkAllEditText(){
        boolean result = false, fullNameResult = false, usernameResult = false, passwordResult = false, password2Result = false;
        if(TextUtils.isEmpty(mEt_fullName.getText().toString())){
            mEt_fullName.setError("This item \"Full name\" cannot be empty.");
            fullNameResult = false;
        } else {
            fullNameResult = true;
        }

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

        if(TextUtils.isEmpty(mEt_password2.getText().toString())){
            mEt_password2.setError("This item \"Retype Password\" cannot be empty.");
            password2Result = false;
        } else {
            password2Result = true;
        }

        if(fullNameResult && usernameResult && passwordResult && password2Result){
            result = true;
        } else {
            result = false;
        }

        return result;
    }

    public boolean checkPassword(){
        boolean result = false;

        if(mEt_password.getText().toString().equals(mEt_password2.getText().toString())){
            result = true;
        } else {
            mEt_password.setError("Two Password are not same.");
            mEt_password2.setError("Two Password are not same.");
            result = false;
        }

        return result;
    }
}
