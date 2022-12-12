package com.example.qlynhanvien.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlynhanvien.R;
import com.example.qlynhanvien.activity.IDepartmentCallBack;
import com.example.qlynhanvien.model.DepartmentEntity;
import com.example.qlynhanvien.viewholder.DepartmentViewHolder;

import java.util.ArrayList;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentViewHolder> {
    private final Context context;
    private ArrayList<DepartmentEntity> listDepartments;
    private final IDepartmentCallBack iDepartmentCallBack; // new mainactivity()
    //refesh DB
    public void setListDepartments(ArrayList<DepartmentEntity> listDepartments){
        this.listDepartments = listDepartments;
        notifyDataSetChanged();
    }
    public DepartmentAdapter(Context context,
                               ArrayList<DepartmentEntity> listDepartments,
                               IDepartmentCallBack iDepartmentCallBack) {
        this.context = context;
        this.listDepartments = listDepartments;
        this.iDepartmentCallBack = iDepartmentCallBack;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_list_layout, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        final DepartmentEntity departments = listDepartments.get(position);
        holder.department.setText(departments.getDepartmentName());

        holder.itemView.setOnClickListener(v -> iDepartmentCallBack.onClick(departments));
        holder.editDepartment.setOnClickListener(view ->
                iDepartmentCallBack.onClickEdit(departments)
        );
        holder.deleteDepartment.setOnClickListener(view -> iDepartmentCallBack.onClickDelete(departments));
    }

    @Override
    public int getItemCount() {
        return listDepartments.size();
    }
}
