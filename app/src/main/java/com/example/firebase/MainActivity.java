package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextInputEditText et_name, et_phone_no, et_address;
    Button btn_save, btn_show;
    FirebaseDatabase fd;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_phone_no = findViewById(R.id.et_phone_no);
        et_address = findViewById(R.id.et_address);
        btn_save = findViewById(R.id.btn_save);
        btn_show = findViewById(R.id.btn_show);

        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference("Employee");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, phone, address;
                name = et_name.getText().toString();
                phone = et_phone_no.getText().toString();
                address = et_address.getText().toString();
                createNewData(name, phone, address);
            }
        });

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ShowEmployee.class);
                startActivity(i);
            }
        });
    }
    public void createNewData(String name, String phone, String address){
        dr.child(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getValue() != null){
                    Snackbar.make(btn_save, "Data already inserted", Snackbar.LENGTH_LONG).show();
                }
                else{
                    dr.child(phone).setValue(new Employee(name, phone, address));
                    Snackbar.make(btn_save, "Insert successfully", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Snackbar.make(btn_save, "Server Error", Snackbar.LENGTH_LONG).show();
            }
        });
    }
}