package com.example.avito;

import static java.lang.String.valueOf;

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
import android.widget.Toast;

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

        getSupportActionBar().hide();

        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        sCategory = findViewById(R.id.sCategory);

        tvTitle.setText("Количество товаров: " + totalItems);
        tvPrice.setText("Стоимость: " + totalCost);

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

        TableLayout dbOutput = findViewById(R.id.tlAnnouncements);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            int userIdIndex = cursor.getColumnIndex(DBHelper.KEY_USERID);
            int uNameIndex = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int catIdIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORYID);
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

                if(MainActivity.UserId == cursor.getInt(userIdIndex)){
                    TextView Void = new TextView(this);
                    params.weight = 1.0f;
                    Void.setLayoutParams(new TableRow.LayoutParams(1, 1));
                    dbOutputRow.addView(Void);
                }
                else{
                    Button deleteBtn = new Button(this);
                    deleteBtn.setOnClickListener(this);
                    params.weight = 1.0f;
                    deleteBtn.setLayoutParams(params);
                    deleteBtn.setText("Добавить");
                    deleteBtn.setId(cursor.getInt(idIndex));
                    dbOutputRow.addView(deleteBtn);
                }

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
            case (R.id.btnBuy):

                if(totalItems != 0){
                    Toast.makeText(this, "Приобретено " + totalItems + " товаров на сумму " + totalCost + " рублей", Toast.LENGTH_LONG).show();
                }

                for(int i = 0; i < basket.length; i++){
                    database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_ID2 + "=" + basket[i], null);
                    //Toast.makeText(this, "" + basket[i], Toast.LENGTH_LONG).show();

                }

                basket = Arrays.copyOf(basket, 0);

                UpdateTable();
            case (R.id.btnSort):
                Cursor cursor = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null,null);

                if(cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID2);
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
                    int userIdIndex = cursor.getColumnIndex(DBHelper.KEY_USERID);
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

                            if(MainActivity.UserId == cursor.getInt(userIdIndex)){
                                TextView Void = new TextView(this);
                                params.weight = 1.0f;
                                Void.setLayoutParams(params);
                                dbOutputRow.addView(Void);
                            }
                            else{
                                Button deleteBtn = new Button(this);
                                deleteBtn.setOnClickListener(this);
                                params.weight = 1.0f;
                                deleteBtn.setLayoutParams(params);
                                deleteBtn.setText("Добавить");
                                deleteBtn.setId(cursor.getInt(idIndex));
                                dbOutputRow.addView(deleteBtn);
                            }
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
                View outputDataBaseRow = (View) v.getParent();
                ViewGroup outputDataBase = (ViewGroup) outputDataBaseRow.getParent();
                int index = outputDataBase.indexOfChild(outputDataBaseRow);
                Cursor cursor2 = database.query(DBHelper.TABLE_ANNOUNCEMENTS, null, null, null, null, null, null);
                if (cursor2 != null) {
                    basket = Arrays.copyOf(basket, basket.length + 1);
                    cursor2.moveToPosition(index);
                    basket[basket.length - 1] = cursor2.getInt(0);
                    totalCost += cursor2.getInt(2);
                    //Toast.makeText(this, "" + cursor2.getString(0), Toast.LENGTH_LONG).show();

                }
                tvPrice.setText("Стоимость: " + totalCost);
                totalItems++;
                tvTitle.setText("Количество товаров: " + totalItems);
                break;
        }
    }
}