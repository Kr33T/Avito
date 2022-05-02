package com.example.avito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class adminActivity2 extends AppCompatActivity implements View.OnClickListener {

    Button btnAddNewUser, btnChangeUser, Transition2, btnDeleteUser, btnToMain2;

    EditText etNewLogin, etNewPassword, etNewName;

    ContentValues contentValues;
    DBHelper dbHelper;
    SQLiteDatabase database;

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin2);

        getSupportActionBar().hide();

        btnAddNewUser = findViewById(R.id.btnAddNewUser);
        btnAddNewUser.setOnClickListener(this);
        btnChangeUser = findViewById(R.id.btnChangeUser);
        btnChangeUser.setOnClickListener(this);
        Transition2 = findViewById(R.id.Transition2);
        Transition2.setOnClickListener(this);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);
        btnDeleteUser.setOnClickListener(this);
        btnToMain2 = findViewById(R.id.btnToMain2);
        btnToMain2.setOnClickListener(this);

        etNewLogin = findViewById(R.id.etNewLogin);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewName = findViewById(R.id.etNewName);

        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        UpdateTable();
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case(R.id.btnToMain2):
                startActivity(new Intent(this, MainActivity.class));
                break;
            case(R.id.btnAddNewUser):

                String login = etNewLogin.getText().toString();
                String password = etNewPassword.getText().toString();
                String name = etNewName.getText().toString();

                if(!login.isEmpty() || !password.isEmpty() || !name.isEmpty()){

                    Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null,null,null, null,null);

                    boolean founded = false;

                    if(cursor.moveToFirst()){
                        int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
                        do{
                            if(login.equals(cursor.getString(loginIndex))){
                                founded = true;
                                break;
                            }
                        }while(cursor.moveToNext());
                    }

                    if(!founded){
                        contentValues = new ContentValues();

                        contentValues.put(DBHelper.KEY_LOGIN, login);
                        contentValues.put(DBHelper.KEY_PASSWORD, password);
                        contentValues.put(DBHelper.KEY_NAME, name);
                        database.insert(DBHelper.TABLE_USERS, null, contentValues);
                    }
                    else{
                        Toast.makeText(this, "Такой пользователь уже зарегистрирован!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(this, "Поля не заполнены!", Toast.LENGTH_SHORT).show();
                }
                UpdateTable();
                break;
            case(R.id.btnChangeUser):
                if(id != 0){
                    String newLogin = etNewLogin.getText().toString();
                    String newPassword = etNewPassword.getText().toString();
                    String newName = etNewName.getText().toString();

                    contentValues = new ContentValues();

                    contentValues.put(DBHelper.KEY_LOGIN, newLogin);
                    contentValues.put(DBHelper.KEY_PASSWORD, newPassword);
                    contentValues.put(DBHelper.KEY_NAME, newName);

                    database.update(DBHelper.TABLE_USERS, contentValues, DBHelper.KEY_ID1 + " = '" + id + "'", null);
                    Toast.makeText(this, "Вы успешно сменили пароль!", Toast.LENGTH_SHORT).show();
                    id = 0;
                }
                else{
                    Toast.makeText(this, "Вы не нажали кнопку USE напротив пользователя, данные которого вы хотите изменить", Toast.LENGTH_SHORT).show();
                }
                UpdateTable();
                break;
            case(R.id.btnDeleteUser):
                if(id != 0){
                    database.delete(DBHelper.TABLE_ANNOUNCEMENTS, DBHelper.KEY_USERID + "=" + id, null);
                    database.delete(DBHelper.TABLE_USERS, DBHelper.KEY_ID1 + "=" + id, null);
                    id = 0;
                }
                else{
                    Toast.makeText(this, "Вы не нажали кнопку USE напротив пользователя, которого хотите удалить", Toast.LENGTH_SHORT).show();
                }
                UpdateTable();
                break;
            case(R.id.Transition2):
                startActivity(new Intent(this, adminActivity.class));
                break;
            default:
                View outputDataBaseRow = (View) v.getParent();
                ViewGroup outputDataBase = (ViewGroup) outputDataBaseRow.getParent();
                int index = outputDataBase.indexOfChild(outputDataBaseRow);
                Cursor cursor2 = database.query(DBHelper.TABLE_USERS, null, null, null, null, null, null);
                if (cursor2 != null) {
                    cursor2.moveToPosition(index);
                    id = cursor2.getInt(0);
                    etNewLogin.setText(cursor2.getString(1));
                    etNewPassword.setText(cursor2.getString(2));
                    etNewName.setText(cursor2.getString(3));
                }
                UpdateTable();
                break;
        }
    }

    public void UpdateTable(){
        Cursor cursor = database.query(DBHelper.TABLE_USERS, null, null, null, null, null,null);

        TableLayout dbOutput = findViewById(R.id.UsersOutput);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID1);
            int loginIndex = cursor.getColumnIndex(DBHelper.KEY_LOGIN);
            int passwordIndex = cursor.getColumnIndex(DBHelper.KEY_PASSWORD);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
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

                TextView outputLogin = new TextView(this);
                params.weight = 1.0f;
                outputLogin.setLayoutParams(params);
                outputLogin.setText(cursor.getString(loginIndex));
                dbOutputRow.addView(outputLogin);

                TextView outputPassword = new TextView(this);
                params.weight = 1.0f;
                outputPassword.setLayoutParams(params);
                outputPassword.setText(cursor.getString(passwordIndex));
                dbOutputRow.addView(outputPassword);

                TextView outputName = new TextView(this);
                params.weight = 1.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                Button useBtn = new Button(this);
                useBtn.setOnClickListener(this);
                params.weight = 1.0f;
                useBtn.setLayoutParams(params);
                useBtn.setText("Use");
                useBtn.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(useBtn);

                dbOutput.addView(dbOutputRow);
            }while(cursor.moveToNext());
        }
        else{
            dbOutput.removeAllViews();
        }
        cursor.close();
    }
}