package com.e.studentslisttodbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class StudentAddingActivity extends AppCompatActivity {
    private EditText fNameET, sNameET, groupET;
    private Boolean isMale;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_adding);

        fNameET = findViewById(R.id.firstNameET);
        sNameET = findViewById(R.id.secondNameET);
        groupET = findViewById(R.id.groupET);
        Bundle arguments = getIntent().getExtras();
        if(arguments!= null){
            student = (Student) arguments.getSerializable(Student.class.getSimpleName());
            fNameET.setText(student.getFirstName());
            sNameET.setText(student.getSecondName());
            groupET.setText(student.getGroupeNumber());
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.maleRB:
                if (checked){
                    isMale = true;
                }
                break;
            case R.id.femaleRB:
                if (checked){
                    isMale = false;
                }
                break;
        }
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.saveClick:
                String fNameStr = fNameET.getText().toString();
                String sNameStr = sNameET.getText().toString();
                String groupStr = groupET.getText().toString();

                int studentId = student == null ? -1 : student.getId();
                Student student = new Student(studentId, fNameStr, sNameStr, groupStr, isMale);
                Intent intentFilled = new Intent();
                intentFilled.putExtra(Student.class.getSimpleName(), student);
                setResult(RESULT_OK, intentFilled);
                finish();

                break;
            case R.id.cancelClick:
                Intent initialIntent = new Intent(this, MainActivity.class);
                setResult(RESULT_CANCELED, initialIntent);
                finish();

                break;
        }
    }
}

