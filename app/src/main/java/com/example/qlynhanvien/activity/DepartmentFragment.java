package com.example.qlynhanvien.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.example.qlynhanvien.adapter.DepartmentAdapter;
import com.example.qlynhanvien.model.DepartmentEntity;
import com.example.qlynhanvien.model.EmployeeEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DepartmentFragment extends Fragment implements IDepartmentCallBack{
    private DepartmentAdapter departmentAdapter;
    private QlyNhanVienDatabase qlyNhanVienDatabase;
    private ArrayList<DepartmentEntity> listDepartmentAll;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_department, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //findByid ..
        RecyclerView departmentView = view.findViewById(R.id.product_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        departmentView.setLayoutManager(linearLayoutManager);
        departmentView.setHasFixedSize(true);
        qlyNhanVienDatabase = QlyNhanVienDatabase.getInstance(getContext());
        if (!isReadDatabase()) {
            readJsonAndInsertDataToDb();
        }
        listDepartmentAll = new ArrayList<>(qlyNhanVienDatabase.departmentDao().getAll());
        if (listDepartmentAll.size() > 0) {
            departmentView.setVisibility(View.VISIBLE); //このビューは非表示ですが、レイアウトのためにスペースを占有します。
            departmentAdapter = new DepartmentAdapter(getContext(), listDepartmentAll,this);
            departmentView.setAdapter(departmentAdapter);
        } else {
            departmentView.setVisibility(View.GONE);    // view.gone このビューは非表示であり、レイアウトのためにスペースを取りません。
        }
    }
    //show dialog to add department
    private void addDepartmentDialog() {
        // covert xml to view in java
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View subView = inflater.inflate(R.layout.add_department_layout, null);
        // find EditText in R.layout.add_department_layout
        final EditText nameField = subView.findViewById(R.id.enter_departmentname);
        // Init Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add new DEPARTMENT");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("ADD DEPARTMENT", (dialog, which) -> {
            final String name = nameField.getText().toString();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "データを入れてください。", Toast.LENGTH_LONG).show();
            } else {
                qlyNhanVienDatabase.departmentDao().insert(name);
                updateNewDataForAdapter();
            }
        });

        builder.setNegativeButton("CANCEL", (dialog, which) -> Toast.makeText(getContext(), "Task cancelled", Toast.LENGTH_LONG).show());
        builder.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_department, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_add:
                addDepartmentDialog();
                break;
            case R.id.search:
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
                // ham search tu ListDepartmentALL => ket qua mong doi sau khi search
                // set lai du lieu vao adapterr

                if (textSearch.isEmpty()) {
                    departmentAdapter.setListDepartments(listDepartmentAll);
                } else {
                    ArrayList<DepartmentEntity> filteredList = new ArrayList<>();
                    for (DepartmentEntity departmentEntity : listDepartmentAll) {
                        if (departmentEntity.getDepartmentName().toLowerCase().contains(textSearch.toLowerCase())) {
                            filteredList.add(departmentEntity);
                        }
                    }
                    departmentAdapter.setListDepartments(filteredList);
                }
                return true;
            }
        });
    }

    //Parse Json to Object
    private void readJsonAndInsertDataToDb() {
        String myJSONStr = loadJSONFromAssets();
        if (myJSONStr == null) {
            return;
        }
        try {
            //Json parsing
            JSONObject rootJsonObject = new JSONObject(myJSONStr);
            JSONArray departmentJsonArray = rootJsonObject.getJSONArray("departments");
            for (int i = 0; i < departmentJsonArray.length(); i++) {
                // Create a temp
                JSONObject departmentObject = departmentJsonArray.getJSONObject(i);
                //get departments details
                String departmentName = departmentObject.getString("departmentname");
                int departmentId = departmentObject.getInt("departmentID");
                qlyNhanVienDatabase.departmentDao().insertOrReplace(departmentId, departmentName);

                //employees array
                JSONArray employeeJsonArray = departmentObject.getJSONArray("employees");
                for (int j = 0; j < employeeJsonArray.length(); j++) {
                    EmployeeEntity aEmployees = new EmployeeEntity();
                    JSONObject EmployeeObject = employeeJsonArray.getJSONObject(j);
                    qlyNhanVienDatabase.employeeDao().insetOrReplace(
                            EmployeeObject.getInt("employeeID"),
                            EmployeeObject.getString("name"),
                            EmployeeObject.getString("phno"),
                            departmentId
                    );
                }
            }
            /// ko co loi
            setIsReadDatabase(true);
        } catch (Exception e) {
            setIsReadDatabase(false);
            e.printStackTrace();
        }
    }

    // JSon
    private String loadJSONFromAssets() {
        String json = null;
        try {
            InputStream inputStream = getContext().getAssets().open("Jsonfile.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return json;
    }

    public void setIsReadDatabase(boolean done) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isRead", done);
        editor.commit();
    }

    public boolean isReadDatabase() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        return sharedPref.getBoolean("isRead", false);
    }




    @Override
    public void onClick(DepartmentEntity department) {
        EmployeeActivity.startSelf(getContext(), department.getId(), department.getDepartmentName());
    }

    @Override
    public void onClickEdit(DepartmentEntity department) {
        editTaskDialog(department);
    }

    @Override
    public void onClickDelete(DepartmentEntity department) {
        deleteTaskDialog(department);
    }


    private void editTaskDialog(final DepartmentEntity departments){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View subView = inflater.inflate(R.layout.add_department_layout, null);

        final EditText nameField = (EditText)subView.findViewById(R.id.enter_departmentname);


        if(departments != null){
            nameField.setText(departments.getDepartmentName());

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit department");
        builder.setView(subView);
        builder.create();

        builder.setPositiveButton("EDIT DEPARTMENT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = nameField.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(getContext(), "Something went wrong. Check your input values", Toast.LENGTH_LONG).show();
                }
                else{
                    qlyNhanVienDatabase.departmentDao().update(departments.getId(),name);
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

    private void updateNewDataForAdapter() {
        listDepartmentAll = new ArrayList<>(qlyNhanVienDatabase.departmentDao().getAll());
        departmentAdapter.setListDepartments(listDepartmentAll);
    }

    public void deleteTaskDialog(final DepartmentEntity departments) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete department");
        builder.setMessage("Are you sure to delete record");
        builder.create();

        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                qlyNhanVienDatabase.departmentDao().delete(departments.getId());
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
