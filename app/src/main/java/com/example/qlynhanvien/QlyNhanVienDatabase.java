package com.example.qlynhanvien;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.qlynhanvien.dao.DepartmentDao;
import com.example.qlynhanvien.dao.EmployeeDao;
import com.example.qlynhanvien.model.DepartmentEntity;
import com.example.qlynhanvien.model.EmployeeEntity;

@Database(entities = {EmployeeEntity.class, DepartmentEntity.class},version = 1,exportSchema = false)
public abstract class QlyNhanVienDatabase extends RoomDatabase {
    public static QlyNhanVienDatabase create(Context context){
        return (QlyNhanVienDatabase) Room.databaseBuilder(context,
                QlyNhanVienDatabase.class,"qlynhanvien_db").allowMainThreadQueries().build();
    }
    private static QlyNhanVienDatabase instance;
    public static QlyNhanVienDatabase getInstance(Context context){
        if (instance==null){
            instance=create(context);
        }return instance;
    }
    public abstract EmployeeDao employeeDao();
    public abstract DepartmentDao departmentDao();
}
