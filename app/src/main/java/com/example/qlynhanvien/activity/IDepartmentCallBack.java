package com.example.qlynhanvien.activity;

import com.example.qlynhanvien.model.DepartmentEntity;

public interface IDepartmentCallBack {
    void onClick(DepartmentEntity department);
    void onClickEdit(DepartmentEntity department);
    void onClickDelete(DepartmentEntity department);
}

