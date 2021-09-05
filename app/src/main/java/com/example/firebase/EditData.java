package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class EditData extends AppCompatActivity {

    String PhoneNumber = "";
    FirebaseDatabase fd;
    DatabaseReference df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        Intent i = getIntent();
        String name = i.getExtras().getString("name");
        String address = i.getExtras().getString("address");
        PhoneNumber  = i.getExtras().getString("ph_no");

        TextInputEditText tv_name = findViewById(R.id.tv_name);
        TextInputEditText tv_addr = findViewById(R.id.tv_addr);
        TextView show_ph_no = findViewById(R.id.show_ph_no);
        Button btn_edit = findViewById(R.id.btn_edit);
        tv_name.setText(name);
        tv_addr.setText(address);
        show_ph_no.setText(PhoneNumber);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tv_name.getText().toString();
                String address = tv_addr.getText().toString();
                updateData(name, address);
            }
        });
    }

    void updateData(String name, String address){
        fd = FirebaseDatabase.getInstance();
        df = fd.getReference("Employee");

        df.child(PhoneNumber).setValue(new Employee(name, PhoneNumber, address));
        Toast.makeText(EditData.this, "Update successfull", Toast.LENGTH_LONG).show();
        finish();
    }
}