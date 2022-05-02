package com.example.avito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etPassword, etName;
    Button btnSignup;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().hide();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);

        btnSignup = findViewById(R.id.btnSignup2);
        btnSignup.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case (R.id.btnSignup2):
                Cursor signupCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean founded = false;
                if(etUsername.getText().toString().equals("admin")){
                    Toast.makeText(this, "Вы не можете использовать логин \"admin\"", Toast.LENGTH_LONG).show();
                    founded = true;
                }
                else{
                    if(etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty() || etName.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Вы не ввели данные, поля пустые!", Toast.LENGTH_LONG).show();
                        founded = true;
                    }
                    else{
                        if (signupCursor.moveToNext()) {
                            int usernameIndex = signupCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                            do {
                                if (etUsername.getText().toString().equals(signupCursor.getString(usernameIndex))) {
                                    Toast.makeText(this, "Пользователь с таким логином уже зарегистрирован!", Toast.LENGTH_LONG).show();
                                    founded = true;
                                    break;
                                }
                            } while (signupCursor.moveToNext());
                        }
                    }
                }
                signupCursor.close();
                if(!founded){
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_LOGIN, etUsername.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORD, etPassword.getText().toString());
                    contentValues.put(DBHelper.KEY_NAME, etName.getText().toString());

                    database.insert(DBHelper.TABLE_USERS, null, contentValues);

                    Toast.makeText(this, "Вы успешно зарегистрировались!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, MainActivity.class));
                }
                break;
        }
    }
}