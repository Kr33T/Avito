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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;

public class BulletinBoard extends AppCompatActivity implements View.OnClickListener{

    TextView tvTitle, tvPrice;
    Spinner sCategory;
    Button btnBuy, btnSort;

    ArrayAdapter<String> adapter;

    Cursor cursor;
    int totalItems = 0;
    int totalCost = 0;

    public int[] basket = new int[0];

    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);

        totalItems = 0;
        totalCost = 0;

        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        sCategory = findViewById(R.id.sCategory);

        btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(this);

        btnSort = findViewById(R.id.btnSort);
        btnSort.setOnClickListener(this);

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
            int uNameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int catIdIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORYID);
            TableLayout dbOutput = findViewById(R.id.tlAnnouncements);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);
                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

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

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(uNameIndex));
                dbOutputRow.addView(outputName);

                TextView outputCat = new TextView(this);
                params.weight = 3.0f;
                outputCat.setLayoutParams(params);
                outputCat.setText(MainActivity.ctg[cursor.getInt(catIdIndex)]);
                dbOutputRow.addView(outputCat);

                Button deleteBtn = new Button(this);
                deleteBtn.setOnClickListener(this);
                params.weight = 1.0f;
                deleteBtn.setLayoutParams(params);
                deleteBtn.setText("Добавить");
                deleteBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(deleteBtn);

                dbOutput.addView(dbOutputRow);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case (R.id.btnBuy):

                View outputDBRow = (View) v.getParent();
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
                    int catIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_CATEGORYID);
                    int userNameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_USERNAME);
                    int realID = 1;
                    do{
                        if(cursorUpdater.getInt(idIndex) > idIndex){
                            contentValues.put(DBHelper.KEY_ID2, realID);
                            contentValues.put(DBHelper.KEY_TITLE, cursorUpdater.getString(titleIndex));
                            contentValues.put(DBHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                            contentValues.put(DBHelper.KEY_CATEGORYID, MainActivity.ctg[cursorUpdater.getInt(catIndex)]);
                            contentValues.put(DBHelper.KEY_USERNAME, cursorUpdater.getString(userNameIndex));
                            database.replace(DBHelper.TABLE_ANNOUNCEMENTS, null, contentValues);
                        }
                        realID++;
                    } while(cursorUpdater.moveToNext());
                    if(cursorUpdater.moveToLast() && (cursorUpdater.getInt(idIndex) == realID)){
                        database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                    }
                    UpdateTable();
                }
                totalItems = 0;
                totalCost = 0;
                tvTitle.setText("Количество товаров: " + totalItems);
                tvPrice.setText("Стоимость: " + totalCost);
                break;
            case (R.id.btnSort):
                Cursor cursor = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null,null);

                if(cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
                    int uNameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
                    int catIdIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORYID);
                    TableLayout dbOutput = findViewById(R.id.tlAnnouncements);
                    dbOutput.removeAllViews();
                    do{
                        TableRow dbOutputRow = new TableRow(this);
                        dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        LinearLayout.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

                        if(cursor.getInt(catIdIndex) == sCategory.getSelectedItemId()){

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

                            TextView outputName = new TextView(this);
                            params.weight = 3.0f;
                            outputName.setLayoutParams(params);
                            outputName.setText(cursor.getString(uNameIndex));
                            dbOutputRow.addView(outputName);

                            TextView outputCat = new TextView(this);
                            params.weight = 3.0f;
                            outputCat.setLayoutParams(params);
                            outputCat.setText(MainActivity.ctg[cursor.getInt(catIdIndex)]);
                            dbOutputRow.addView(outputCat);

                            Button addBtn = new Button(this);
                            addBtn.setOnClickListener(this);
                            params.weight = 1.0f;
                            addBtn.setLayoutParams(params);
                            addBtn.setText("Добавить");
                            addBtn.setId(cursor.getInt(idIndex));
                            dbOutputRow.addView(addBtn);

                            dbOutput.addView(dbOutputRow);
                        }
                    }while(cursor.moveToNext());
                }
                cursor.close();
                totalItems = 0;
                totalCost = 0;
                tvTitle.setText("Количество товаров: " + totalItems);
                tvPrice.setText("Стоимость: " + totalCost);
                break;
            default:
                totalItems++;
                int temp = 0;
                cursor = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null,null,null,null,null,null);
                basket = Arrays.copyOf(basket, basket.length + 1);
                if(cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
                    int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);

                    do {
                        temp = cursor.getInt(priceIndex);
                        basket[basket.length - 1] = cursor.getInt(idIndex);

                    }while(cursor.moveToNext());
                }
                totalCost = temp;
                tvTitle.setText("Количество товаров: " + totalItems);
                tvPrice.setText("Стоимость: " + totalCost);
                break;
        }
    }
}