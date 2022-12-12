package com.example.qlynhanvien.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employee")
public class EmployeeEntity {

    @PrimaryKey(autoGenerate = true)
    private	int	id;

    @ColumnInfo(name = "employee_name")
    private	String employeeName;

    @ColumnInfo(name = "phone_number")
    private	String phoneNumber;

    @ColumnInfo(name = "department_id")
    private int departmentId ;

    public EmployeeEntity() {
        this.employeeName = employeeName;
        this.phoneNumber = phoneNumber;

    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



}
