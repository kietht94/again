package com.example.qlynhanvien.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qlynhanvien.R;
import com.example.qlynhanvien.model.DepartmentEntity;

import java.util.List;

public class SpinnerDepartmentAdapter extends ArrayAdapter<DepartmentEntity> {

    public SpinnerDepartmentAdapter(@NonNull Context context, int resource, @NonNull List<DepartmentEntity> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected,parent,false);
        TextView tvCategory = convertView.findViewById(R.id.tv_selected);

        DepartmentEntity category = this.getItem(position);
        if (category!=null){
            tvCategory.setText(category.getDepartmentName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        TextView tvCategory = convertView.findViewById(R.id.tv_category);

        DepartmentEntity category = this.getItem(position);
        if (category!=null){
            tvCategory.setText(category.getDepartmentName());
        }
        return convertView;
    }
}

