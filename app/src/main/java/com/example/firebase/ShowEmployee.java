package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowEmployee extends AppCompatActivity {

    ListView show_employee_list;
    ArrayList<Employee> employeeList ;
    FirebaseDatabase fd;
    DatabaseReference dr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_employee);

        show_employee_list = findViewById(R.id.show_employee_list);

        fd = FirebaseDatabase.getInstance();
        dr = fd.getReference("Employee");

        getAllEmployee();

    }

    private void getAllEmployee(){
        employeeList = new ArrayList<>();
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                employeeList.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Employee emp = ds.getValue(Employee.class);
                    employeeList.add(emp);
                }
                if(employeeList.size()==0){
                    Toast.makeText(ShowEmployee.this, "Employee data was empty", Toast.LENGTH_LONG).show();
                }
                else{
                    show_employee_list.setAdapter(new EmployeeListAdapter());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ShowEmployee.this, "Server error", Toast.LENGTH_LONG).show();
            }
        });

    }

    class EmployeeListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return employeeList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater li = getLayoutInflater();
            View row = li.inflate(R.layout.employee_list_design, null);
            TextView show_name = row.findViewById(R.id.show_name);
            show_name.setText(employeeList.get(i).getName());
            TextView show_phone_no = row.findViewById(R.id.show_phone_no);
            show_phone_no.setText(employeeList.get(i).getPhone_number());
            TextView show_adress = row.findViewById(R.id.show_adress);
            show_adress.setText(employeeList.get(i).getAddress());

            Button edit = row.findViewById(R.id.edit);
            ImageButton delete = row.findViewById(R.id.delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShowEmployee.this, EditData.class);
                    intent.putExtra("name", employeeList.get(i).getName());
                    intent.putExtra("ph_no", employeeList.get(i).getPhone_number());
                    intent.putExtra("address", employeeList.get(i).getAddress());
                    startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String PhoneNo = employeeList.get(i).getPhone_number();
                    AlertDialog.Builder dilog = new AlertDialog.Builder(ShowEmployee.this);
                    dilog.setTitle("Confirm Delete");
                    dilog.setMessage("Are you sure to delete "+PhoneNo+" record?");
                    dilog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dilog.setCancelable(true);
                            FirebaseDatabase fd = FirebaseDatabase.getInstance();
                            DatabaseReference dr = fd.getReference("Employee");
                            dr.child(PhoneNo).removeValue();
                            Toast.makeText(ShowEmployee.this, "Delete successfully", Toast.LENGTH_LONG).show();
                        }
                    });
                    dilog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dilog.setCancelable(true);
                        }
                    });
                    dilog.show();
                }
            });
            return row;
        }
    }
}