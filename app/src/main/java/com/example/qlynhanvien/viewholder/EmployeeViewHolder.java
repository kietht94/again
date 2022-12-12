package com.example.qlynhanvien.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qlynhanvien.R;

public class EmployeeViewHolder extends RecyclerView.ViewHolder {
    public TextView name,phone;
    public ImageView deleteEmployee;
    public ImageView editEmployee;
    public EmployeeViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.employee_name);
        phone= (TextView)itemView.findViewById(R.id.ph_no);
        deleteEmployee = (ImageView)itemView.findViewById(R.id.delete_employee);
        editEmployee = (ImageView)itemView.findViewById(R.id.edit_employee);
    }
}
