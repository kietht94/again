package com.example.qlynhanvien.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.qlynhanvien.model.DepartmentEntity;

import java.util.List;

@Dao
public interface DepartmentDao {
    @Query("SELECT * FROM department")
    List<DepartmentEntity> getAll();


    @Query("INSERT OR REPLACE INTO department ('id','department_name') VALUES(:id,:departmentName)")
    void insertOrReplace(int id,String departmentName);

    @Query("INSERT INTO department('department_name') VALUES(:departmentName)")
    void insert(String departmentName);

    @Query("UPDATE department SET department_name=:departmentName WHERE id = :id")
    void update(int id, String departmentName);

    @Query("DELETE FROM department WHERE id=:id")
    void delete(int id);


}
