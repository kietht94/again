package com.example.qlynhanvien.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlynhanvien.QlyNhanVienDatabase;
import com.example.qlynhanvien.R;
import com.example.qlynhanvien.adapter.EmployeeAdapter;
import com.example.qlynhanvien.model.EmployeeEntity;

import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements IEmployeeCallBack {
    //pass du lieu giua cac man hinh
    public static void startSelf(Context context, int departmentId, String departmentName) {
        Intent intent = new Intent(context, EmployeeActivity.class);
        intent.putExtra("departmentId", departmentId);
        intent.putExtra("departmentname", departmentName);
        context.startActivity(intent);
    }

    private EmployeeAdapter employeeAdapter;
    private int departmentId = -99;
    private QlyNhanVienDatabase qlyNhanVienDatabase;
    private String departmentName = null;
    private ArrayList<EmployeeEntity>listEmployeeAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        qlyNhanVienDatabase = QlyNhanVienDatabase.getInstance(getApplicationContext());
        departmentId = getIntent().getIntExtra("departmentId", -99);
        departmentName = getIntent().getStringExtra("departmentname");
        if (departmentId == -99) {
            finish();
        }
        RecyclerView listView = findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(this));
        ///  doc du lieu tu db
        listEmployeeAll = new ArrayList<>(qlyNhanVienDatabase.employeeDao().getAllByDepartmentId(departmentId));
        employeeAdapter = new EmployeeAdapter(this, listEmployeeAll,this);
        listView.setAdapter(employeeAdapter);

        Toolbar actionBarToolBar = (Toolbar) findViewById(R.id.toolbar_employee);
        setSupportActionBar(actionBarToolBar);
        ///
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> addEmployeeDialog());

        getSupportActionBar().setTitle(departmentName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarToolBar.setNavigationOnClickListener(v -> {
            //What to do on back clicked
            finish();
        });
    }


    public void addEmployeeDialog() {
        // da chon dartppaetment r
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);
        final EditText nameField = subView.findViewById(R.id.enter_employeename);
        final EditText phoneField = subView.findViewById(R.id.enter_phno);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new Employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD Employee", (dialog, which) -> {
            final String name = nameField.getText().toString();
            final String phone = phoneField.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(EmployeeActivity.this, "データを入れてください。", Toast.LENGTH_LONG).show();
            } else {
                qlyNhanVienDatabase.employeeDao().insert(name, phone,departmentId);
                updateNewDataForAdapter();
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> Toast.makeText(EmployeeActivity.this, "Task cancelled", Toast.LENGTH_LONG).show());
        builder.show();
    }

    private void updateNewDataForAdapter() {
        listEmployeeAll =  new ArrayList<>(qlyNhanVienDatabase.employeeDao().getAllByDepartmentId(departmentId));
        employeeAdapter.setListEmployees(listEmployeeAll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_add_employee:
                addEmployeeDialog();
                break;
            case R.id.search_employee:
                SearchView searchView = (SearchView) item.getActionView();
                search(searchView);
                break;
        }
        return true;

    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String textSearch) {
                if (textSearch.isEmpty()) {
                    employeeAdapter.setListEmployees(listEmployeeAll);
                } else {
                    ArrayList<EmployeeEntity> filteredList = new ArrayList<>();
                    for (EmployeeEntity employeeEntity : listEmployeeAll) {
                        if (employeeEntity.getEmployeeName().toLowerCase().contains(textSearch.toLowerCase())) {
                            filteredList.add(employeeEntity);
                        }
                    }
                    employeeAdapter.setListEmployees(filteredList);
                }
                return true;
            }
        });
    }

    @Override
    public void onClickEdit(EmployeeEntity employee) {
        editTaskDialog(employee);
    }

    @Override
    public void onClickDelete(EmployeeEntity employee) {
        deleteTaskDialog(employee);
    }

    private void editTaskDialog(final EmployeeEntity employees){
        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.add_employee_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_employeename);
        final EditText phoneField = (EditText)subView.findViewById(R.id.enter_phno);

        if(employees != null){
            nameField.setText(employees.getEmployeeName());
            phoneField.setText(String.valueOf(employees.getPhoneNumber()));

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT EMPLOYEE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String phone = phoneField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(EmployeeActivity.this, "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    qlyNhanVienDatabase.employeeDao().update(employees.getId(),name,phone);
                    //refresh the activity
                    updateNewDataForAdapter();

                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EmployeeActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    public void deleteTaskDialog(final EmployeeEntity employees) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete employee");
        builder.setMessage("Are you sure to delete record");
        builder.create();

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qlyNhanVienDatabase.employeeDao().delete(employees.getId());
                //refresh the activity
                updateNewDataForAdapter();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(EmployeeActivity.this, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

    }
}
