package com.example.avito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mainMenu extends AppCompatActivity implements View.OnClickListener{

    Button myAnnounce, bulletinBoard;
    TextView tv;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        myAnnounce = findViewById(R.id.myAnnounce);
        myAnnounce.setOnClickListener(this);

        bulletinBoard = findViewById(R.id.bulletinBoard);
        bulletinBoard.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            int id = cursor.getColumnIndex(DBHelper.KEY_ID1);
            int uName = cursor.getColumnIndex(DBHelper.KEY_NAME);

            do{
                if(MainActivity.UserId == cursor.getInt(id)) {
                    userName = cursor.getString(uName);
                    break;
                }
            }while(cursor.moveToNext());
        }

        tv = findViewById(R.id.textView);
        tv.setText(String.valueOf("ID аккаунта: " + MainActivity.UserId + "\nЗдравствуйте, " + userName));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case (R.id.myAnnounce):
                startActivity(new Intent(this, MyAnnouncement.class));
                break;
            case(R.id.bulletinBoard):
                startActivity(new Intent(this, BulletinBoard.class));
                break;
        }
    }
}