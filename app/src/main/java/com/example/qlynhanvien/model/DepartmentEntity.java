package com.example.qlynhanvien.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "department")
public class DepartmentEntity {

    @PrimaryKey(autoGenerate = true)
    private	int	id;

    @ColumnInfo(name = "department_name")
    private	String departmentName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
