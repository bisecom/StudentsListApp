package com.e.studentslisttodbapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DbWorker dbWorker;
    private ListView studentsListView;

    private static  final int REQUEST_ACCESS_TYPE_ADD = 1;
    private static  final int REQUEST_ACCESS_TYPE_EDIT = 2;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbWorker = new DbWorker(getApplicationContext());
        studentAdapter = dbWorker.getDataFromDb();
        studentsListView = findViewById(R.id.studentsList);
        studentsListView.setAdapter(studentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.adding_settings :
                Intent intent = new Intent(this, StudentAddingActivity.class);
                startActivityForResult(intent, REQUEST_ACCESS_TYPE_ADD);
                return true;
            case R.id.deleting_settings:
                studentAdapter.removeCheckedLine();
                dbWorker.deleteStudents(studentAdapter.itemsToDelete);
                studentAdapter.itemsToDelete.clear();
                return true;
            case R.id.updating_settings:
                if(studentAdapter.itemsToDelete.size() != 1){
                    Toast.makeText(this, "Check one student only!",Toast.LENGTH_LONG).show();
                    break;
                }
                Student student = null;
                for(Student std : studentAdapter.students) {
                    if(std.getId() == (studentAdapter.itemsToDelete.get(0))) {
                        student = std;
                    }
                }
                if(student == null) break;
                Intent intentEdit = new Intent(this, StudentAddingActivity.class);
                intentEdit.putExtra(Student.class.getSimpleName(), student);
                startActivityForResult(intentEdit, REQUEST_ACCESS_TYPE_EDIT);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ACCESS_TYPE_ADD) {
            if (resultCode == RESULT_OK) {
                Student newStudent = (Student)data.getSerializableExtra(Student.class.getSimpleName());
                int id = dbWorker.insertStudent(newStudent);
                newStudent.setId(id);
                studentAdapter.addingStudent(newStudent);
            }
        }
        if (requestCode == REQUEST_ACCESS_TYPE_EDIT) {
            if (resultCode == RESULT_OK) {
                Student newStudent = (Student)data.getSerializableExtra(Student.class.getSimpleName());
                studentAdapter.updateStudent(newStudent);
                dbWorker.updateStudent(newStudent);
                studentAdapter.itemsToDelete.clear();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
