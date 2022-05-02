package com.example.avito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MyAnnouncement extends AppCompatActivity implements View.OnClickListener{

    EditText etTitle, etPrice;
    Spinner sCategory;
    Button btnAdd;

    ArrayAdapter<String> adapter;

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_announcement);

        getSupportActionBar().hide();

        etTitle = findViewById(R.id.etTitle);
        etPrice = findViewById(R.id.etPrice);
        sCategory = findViewById(R.id.sCategory);

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MainActivity.ctg);
        sCategory.setAdapter(adapter);

        UpdateTable();
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null,null);

        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int userIdIndex = cursor.getColumnIndex(DBHelper.KEY_USERID);
            TableLayout dbOutput = findViewById(R.id.tlAnnouncements);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                if(MainActivity.UserId == cursor.getInt(userIdIndex)){
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

                    TextView outputPrice = new TextView(this);
                    params.weight = 3.0f;
                    outputPrice.setLayoutParams(params);
                    outputPrice.setText(cursor.getString(priceIndex));
                    dbOutputRow.addView(outputPrice);

                    Button deleteBtn = new Button(this);
                    deleteBtn.setOnClickListener(this);
                    params.weight = 1.0f;
                    deleteBtn.setLayoutParams(params);
                    deleteBtn.setText("Удалить");
                    deleteBtn.setId(cursor.getInt(idIndex));
                    dbOutputRow.addView(deleteBtn);

                    dbOutput.addView(dbOutputRow);
                }
            }while(cursor.moveToNext());
        }
        cursor.close();
    }



    @Override
    public void onClick(View v){
        switch(v.getId()){
            case (R.id.btnAdd):
                String title = etTitle.getText().toString();
                String price = etPrice.getText().toString();

                contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_TITLE, title);
                contentValues.put(DBHelper.KEY_PRICE, price);
                contentValues.put(DBHelper.KEY_USERID, MainActivity.UserId);
                contentValues.put(DBHelper.KEY_CATEGORYID, sCategory.getSelectedItemId());
                contentValues.put(DBHelper.KEY_USERNAME, mainMenu.userName);

                database.insert(DBHelper.TABLE_ANNOUNCEMENTS, null, contentValues);
                UpdateTable();
                break;
            default:

                View outputDataBaseRow = (View) v.getParent();
                ViewGroup outputDataBase = (ViewGroup) outputDataBaseRow.getParent();
                int index = outputDataBase.indexOfChild(outputDataBaseRow);
                Cursor cursor2 = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null, null);
                if (cursor2 != null) {
                    cursor2.moveToPosition(index);
                    database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + "=" + cursor2.getInt(0), null);
                }
                UpdateTable();

                /*View outputDBRow = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                outputDB.removeView(outputDBRow);
                outputDB.invalidate();

                database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + " = ?", new String[]{String.valueOf(v.getId())});

                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID2);
                    int titleIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_TITLE);
                    int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex) > idIndex){
                            contentValues.put(DBHelper.KEY_ID2, realID);
                            contentValues.put(DBHelper.KEY_TITLE, cursorUpdater.getString(titleIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                            database.replace(DBHelper.TABLE_ANNOUNCEMENTS, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }*/
                break;
        }
    }
}