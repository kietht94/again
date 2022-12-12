package com.example.qlynhanvien.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.qlynhanvien.model.EmployeeEntity;

import java.util.List;

@Dao
public interface EmployeeDao {
    @Query("SELECT * FROM employee")
    List<EmployeeEntity>getAll();

    @Query("SELECT * FROM employee WHERE department_id = :departmentId")
    List<EmployeeEntity> getAllByDepartmentId(int departmentId);
    //employee_activity
    @Query("INSERT OR REPLACE INTO employee ('id', 'employee_name','phone_number','department_id') VALUES (:id, :employeeName,:phoneNumber,:departmentId)" )
    void insetOrReplace(int id, String employeeName, String phoneNumber, int departmentId);
    //employee_fragment
    @Query("INSERT OR REPLACE INTO employee ( 'employee_name','phone_number','department_id') VALUES ( :employeeName,:phoneNumber,:departmentId)" )
    void insert(String employeeName, String phoneNumber, int departmentId);

    @Query("UPDATE employee SET employee_name=:employeeName, phone_number=:phoneNumber WHERE id = :id")
    void update(int id,String employeeName, String phoneNumber );

    @Query("UPDATE employee SET employee_name=:employeeName, phone_number=:phoneNumber, department_id=:deparmentId WHERE id = :id")
    void update(int id,String employeeName, String phoneNumber, int deparmentId );

    @Query("DELETE FROM employee WHERE id=:id")
    void delete(int id);
}
