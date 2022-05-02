package com.example.avito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class profile extends AppCompatActivity implements View.OnClickListener{

    EditText etUserName, etPassWord;
    Button btnChange;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();

        Toast.makeText(this, "Внимание!\nПосле смены пароля вам необходимо будет авторизироваться заново!", Toast.LENGTH_SHORT).show();

        etUserName = findViewById(R.id.etUserName);
        etPassWord = findViewById(R.id.etPassWord);
        btnChange = findViewById(R.id.btnChange);
        btnChange.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_USERS + " where " + DBHelper.KEY_ID1 + " = '" + MainActivity.UserId + "'", null);

        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
        int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);

        if(cursor.moveToFirst()){
            etUserName.setText(cursor.getString(loginIndex));
            etPassWord.setText(cursor.getString(passwordIndex));
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case(R.id.btnChange):
                String login = etUserName.getText().toString();
                String password = etPassWord.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_LOGIN, login);
                contentValues.put(DBHelper.KEY_PASSWORD, password);

                database.update(DBHelper.TABLE_USERS, contentValues, DBHelper.KEY_ID1 + " = '" + MainActivity.UserId + "'", null);
                Toast.makeText(this, "Вы успешно сменили пароль!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}