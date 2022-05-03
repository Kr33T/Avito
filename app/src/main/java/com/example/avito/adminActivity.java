package com.example.avito;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class adminActivity extends AppCompatActivity implements View.OnClickListener {

    //ready

    Button btnToMain, btnTransition;

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportActionBar().hide();

        btnToMain = findViewById(R.id.btnToMain);
        btnToMain.setOnClickListener(this);
        btnTransition = findViewById(R.id.Transition);
        btnTransition.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        UpdateTable();
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null,null);

        TableLayout dbOutput = findViewById(R.id.dbOutput);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int categoryIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORYID);
            int usNameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputTitle = new TextView(this);
                params.weight = 3.0f;
                outputTitle.setLayoutParams(params);
                outputTitle.setText(cursor.getString(titleIndex));
                dbOutputRow.addView(outputTitle);

                TextView outputCategory = new TextView(this);
                params.weight = 3.0f;
                outputCategory.setLayoutParams(params);
                outputCategory.setText(MainActivity.ctg[cursor.getInt(categoryIndex)]);
                dbOutputRow.addView(outputCategory);

                TextView outputUserName = new TextView(this);
                params.weight = 3.0f;
                outputUserName.setLayoutParams(params);
                outputUserName.setText(cursor.getString(usNameIndex));
                dbOutputRow.addView(outputUserName);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Удалить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            }while(cursor.moveToNext());
        }
        else{
            dbOutput.removeAllViews();
        }
        cursor.close();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case (R.id.btnToMain):
                startActivity(new Intent(this, MainActivity.class));
                break;
            case (R.id.Transition):
                startActivity(new Intent(this, adminActivity2.class));
                break;
            default:
                View outputDataBaseRow = (View) v.getParent();
                ViewGroup outputDataBase = (ViewGroup) outputDataBaseRow.getParent();
                int index = outputDataBase.indexOfChild(outputDataBaseRow);
                @SuppressLint("Recycle") Cursor cursor2 = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null, null);
                if (cursor2 != null) {
                    cursor2.moveToPosition(index);
                    database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + "=" + cursor2.getInt(0), null);
                }
                UpdateTable();
                break;
        }
    }
}