package com.example.qlynhanvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlynhanvien.QlyNhanVienDatabase;
import com.example.qlynhanvien.R;
import com.example.qlynhanvien.activity.IEmployeeCallBack;
import com.example.qlynhanvien.model.EmployeeEntity;
import com.example.qlynhanvien.viewholder.EmployeeViewHolder;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeViewHolder> {
    private Context context;
    private ArrayList<EmployeeEntity> listEmployees;
    private ArrayList<EmployeeEntity> mArrayList;
    private QlyNhanVienDatabase qlyNhanVienDatabase;
    private IEmployeeCallBack iEmployeeCallBack;

    public void setListEmployees(ArrayList<EmployeeEntity>listEmployees){
        this.listEmployees=listEmployees;
        notifyDataSetChanged();
    }
    public EmployeeAdapter (Context context,ArrayList<EmployeeEntity>listEmployees,
                            IEmployeeCallBack iEmployeeCallBack){
        this.context=context;
        this.listEmployees=listEmployees;
        this.mArrayList=listEmployees;
        this.iEmployeeCallBack=iEmployeeCallBack;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_layout,parent,false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        final EmployeeEntity employees = listEmployees.get(position);
        holder.name.setText(employees.getEmployeeName());
        holder.phone.setText(employees.getPhoneNumber());
        holder.editEmployee.setOnClickListener(view-> iEmployeeCallBack.onClickEdit(employees));
        holder.deleteEmployee.setOnClickListener(view->iEmployeeCallBack.onClickDelete(employees));
    }

    @Override
    public int getItemCount() {
        return listEmployees.size();
    }
}
