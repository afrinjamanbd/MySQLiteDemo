package com.example.mithundas.databasepresentation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MyDatabaseHelper myDatabaseHelper;

    EditText NameEditText,AgeEditText,GenderEditText,Id;
    Button SubmitBtn,ShowDataBtn,UpdateData,DeleteData;
    SQLiteDatabase db;

    public void open(){
        this.db = myDatabaseHelper.getWritableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHelper = new MyDatabaseHelper(this);
        db = myDatabaseHelper.getWritableDatabase();

        NameEditText = findViewById(R.id.nameId);
        AgeEditText = findViewById(R.id.ageId);
        GenderEditText = findViewById(R.id.genderId);
        Id = findViewById(R.id.Id);
        SubmitBtn = findViewById(R.id.submitBtnId);
        ShowDataBtn = findViewById(R.id.ShowBtnId);
        UpdateData = findViewById(R.id.UpdateBtnId);
        DeleteData = findViewById(R.id.DeleteBtnId);

        SubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Name = NameEditText.getText().toString();
                String Age = AgeEditText.getText().toString();
                String Gender = GenderEditText.getText().toString();
                long rowId = myDatabaseHelper.InsertData(Name,Age,Gender);

                if (rowId == -1){
                    Toast.makeText(getApplicationContext(),"insert unsuccessful",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),"insert successful",Toast.LENGTH_LONG).show();
                }


            }
        });

        ShowDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Cursor cursor = myDatabaseHelper.DisplayAllData();

               if(cursor.getCount()== 0){
                   showData("Error", "NO DATA FOUND");
               }
               else {
                   StringBuilder stringBuffer = new StringBuilder();
                   while (cursor.moveToNext()){
                       stringBuffer.append("ID :").append(cursor.getString(0)).append("\t");
                       stringBuffer.append("NAME :").append(cursor.getString(1)).append("\t");
                       stringBuffer.append("AGE :").append(cursor.getString(2)).append("\t");
                       stringBuffer.append("GENDER :").append(cursor.getString(3)).append("\n").append("\n");

                   }
                   showData("All DATA",stringBuffer.toString() );
               }
            }
        });

        UpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
                String id = Id.getText().toString();
                String name= NameEditText.getText().toString();
                String age = AgeEditText.getText().toString();
                String gender = GenderEditText.getText().toString();
                boolean isUpdated = myDatabaseHelper.UpdateData(id,name,age,gender);

                if(isUpdated){
                    Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Not Updated",Toast.LENGTH_LONG).show();
                }
            }
        });

        DeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open();
                String id =Id.getText().toString();
                int value = myDatabaseHelper.DeleteData(id);

                if(value > 0){
                    Toast.makeText(getApplicationContext(),"Data Deleted",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Data is not Deleted",Toast.LENGTH_LONG).show();

                }
            }
        });


    }

    public void showData(String title,String data){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(data);
        builder.setCancelable(true);
        builder.show();
    }

    @Override
    protected void onDestroy() {
        myDatabaseHelper.close();
        super.onDestroy();
    }
}
