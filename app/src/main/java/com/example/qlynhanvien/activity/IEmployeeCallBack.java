package com.example.qlynhanvien.activity;

import com.example.qlynhanvien.model.EmployeeEntity;

public interface IEmployeeCallBack {
    void onClickEdit(EmployeeEntity employee);

    void onClickDelete(EmployeeEntity employee);
}