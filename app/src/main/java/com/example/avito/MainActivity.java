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

    EditText etUsername, etPassword;
    Button btnLogin, btnSignup;

    DBHelper dbHelper;
    SQLiteDatabase database;

    ContentValues contentValues;

    public static int UserId;
    public static String[] ctg;
    public static String[] tables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        contentValues = new ContentValues();
        tables = new String[] {"Объявления", "Пользователи"};
        ctg = new String[] {"Недвижимость", "Автомобили", "Спорт", "Хобби"};
        for (int i = 0; i < ctg.length; i++){
            contentValues.put(DBHelper.KEY_CATEGORY, ctg[i]);
            database.insert(DBHelper.TABLE_CATEGORIES, null, contentValues);
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
                startActivity(new Intent(this, Registration.class));
                break;
        }
    }
}