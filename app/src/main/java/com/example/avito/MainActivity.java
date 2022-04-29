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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etPassword, etName;
    Button btnLogin, btnSignup;

    DBHelper dbHelper;
    SQLiteDatabase database;

    ContentValues contentValues;

    public static int UserId;
    public static String[] ctg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        etUsername = findViewById(R.id.etUsername);
        etUsername.setOnFocusChangeListener(this::onFocusChanged);
        etPassword = findViewById(R.id.etPassword);
        etPassword.setOnFocusChangeListener(this::onFocusChanged);
        etName = findViewById(R.id.etName);
        etName.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        contentValues = new ContentValues();
        ctg = new String[] {"Недвижимость", "Автомобили", "Спорт", "Хобби"};
        for (int i = 0; i < ctg.length; i++){
            contentValues.put(DBHelper.KEY_CATEGORY, ctg[i]);
            database.insert(DBHelper.TABLE_CATEGORIES, null, contentValues);
        }
    }

    public void onFocusChanged(View view, boolean b){
        switch(view.getId()){
            case R.id.etUsername:
                if(b){
                    etUsername.setHint("");
                }
                else{
                    etUsername.setHint("Username");
                }
                break;
            case R.id.etPassword:
                if(b){
                    etPassword.setHint("");
                }
                else{
                    etPassword.setHint("Password");
                }
                break;
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnLogin:
                Cursor loginCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean reg = false;

                if(etUsername.getText().toString().equals("admin") && etPassword.getText().toString().equals("admin")){
                    Toast.makeText(this, "Вы вошли в управление базой данных", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, adminActivity.class));
                }
                else{
                    if(loginCursor.moveToFirst()){
                        int usernameIndex = loginCursor.getColumnIndex(DBHelper.KEY_LOGIN);
                        int passwordIndex = loginCursor.getColumnIndex(DBHelper.KEY_PASSWORD);
                        int id = loginCursor.getColumnIndex(DBHelper.KEY_ID1);
                        do{
                            if(etUsername.getText().toString().equals(loginCursor.getString(usernameIndex)) && etPassword.getText().toString().equals(loginCursor.getString(passwordIndex))){
                                UserId = loginCursor.getInt(id);
                                startActivity(new Intent(this, mainMenu.class));
                                reg = true;
                                break;
                            }
                        }while(loginCursor.moveToNext());

                    }
                    loginCursor.close();
                    if(!reg){
                        Toast.makeText(this, "Введены некорректные данные!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.btnSignup:
                Cursor signupCursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);

                boolean founded = false;
                if(etUsername.getText().toString().equals("admin")){
                    Toast.makeText(this, "Вы не можете использовать логин \"admin\"", Toast.LENGTH_LONG).show();
                    founded = true;
                }
                else{
                    if(etUsername.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()) {
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
                }
                break;
        }
    }
}