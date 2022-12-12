package com.example.qlynhanvien.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlynhanvien.QlyNhanVienDatabase;
import com.example.qlynhanvien.R;
import com.example.qlynhanvien.adapter.EmployeeAdapter;
import com.example.qlynhanvien.adapter.SpinnerDepartmentAdapter;
import com.example.qlynhanvien.model.DepartmentEntity;
import com.example.qlynhanvien.model.EmployeeEntity;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFragment extends Fragment implements IEmployeeCallBack {

    // QQery all .
    private EmployeeAdapter employeeAdapter;
    private QlyNhanVienDatabase qlyNhanVienDatabase;
    private ArrayList<EmployeeEntity> listEmployeeAll;
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_employee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // spinner
//        departmentV2Adapter = new DepartmentV2Adapter(getContext(), listDepartmentAll,this);
//        spinner.setAdapter(departmentV2Adapter);
        RecyclerView listView = view.findViewById(R.id.product_listemployee);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        listView.setLayoutManager(linearLayoutManager);
        listView.setHasFixedSize(true);
        qlyNhanVienDatabase = qlyNhanVienDatabase.getInstance(getContext());
        ///  doc du lieu tu db
        listEmployeeAll = new ArrayList<>(qlyNhanVienDatabase.employeeDao().getAll());
        employeeAdapter = new EmployeeAdapter(getContext(), listEmployeeAll,this);
        listView.setAdapter(employeeAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateNewDataForAdapter();
    }

    public void addEmployeeDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View subView = inflater.inflate(R.layout.add_list_employee_layout, null);
        final EditText nameField = subView.findViewById(R.id.enter_employeename);
        final EditText phoneField = subView.findViewById(R.id.enter_phone);
        final Spinner spinner = subView.findViewById(R.id.spinner);
        List<DepartmentEntity> list = qlyNhanVienDatabase.departmentDao().getAll();
        SpinnerDepartmentAdapter departmentV2Adapter = new SpinnerDepartmentAdapter(requireContext(),R.layout.item_selected ,list);
        spinner.setAdapter(departmentV2Adapter);
        final DepartmentEntity[] departmentSelected = {list.get(0)};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentSelected[0] = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add new Employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD Employee", (dialog, which) -> {
            final String name = nameField.getText().toString();
            final String phone = phoneField.getText().toString();

            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "データを入れてください。", Toast.LENGTH_LONG).show();
            } else {
                //add employee_list
                int departmentId = departmentSelected[0].getId();
                qlyNhanVienDatabase.employeeDao().insert(name, phone,departmentId);
                updateNewDataForAdapter();
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> Toast.makeText(getContext(), "Task cancelled", Toast.LENGTH_LONG).show());
        builder.show();
    }

    private void updateNewDataForAdapter() {
        listEmployeeAll =  new ArrayList<>(qlyNhanVienDatabase.employeeDao().getAll());
        if (employeeAdapter!=null) {
            employeeAdapter.setListEmployees(listEmployeeAll);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list_employee, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.btn_add_employee:
            addEmployeeDialog();
                break;
            case R.id.search_list_employee:
                SearchView searchView = (SearchView) item.getActionView();
                search(searchView);
                break;
        }
        return false;

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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View subView = inflater.inflate(R.layout.add_list_employee_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_employeename);
        final EditText phoneField = (EditText)subView.findViewById(R.id.enter_phone);
        final Spinner spinner = subView.findViewById(R.id.spinner);
        List <DepartmentEntity> list = qlyNhanVienDatabase.departmentDao().getAll();
        int position = 0 ;
        for (int i =0 ;i < list.size();i++){
            if (list.get(i).getId() == employees.getDepartmentId()){
                position = i;
            }
        }

        SpinnerDepartmentAdapter adapter = new SpinnerDepartmentAdapter(requireContext(),R.layout.item_selected,list );
        spinner.setAdapter(adapter);
        spinner.setSelection(position);
        final DepartmentEntity[] departmentSelection = {list.get(position)};// mac dinh hien thi

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                departmentSelection[0] = list.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(employees != null){
            nameField.setText(employees.getEmployeeName());
            phoneField.setText(String.valueOf(employees.getPhoneNumber()));

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit employee");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT EMPLOYEE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();
                final String phone = phoneField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getContext(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    qlyNhanVienDatabase.employeeDao().update(employees.getId(),name,phone,departmentSelection[0].getId());
                    //refresh the activity
                    updateNewDataForAdapter();

                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    public void deleteTaskDialog(final EmployeeEntity employees) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                Toast.makeText(getContext(), "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();

    }
}
