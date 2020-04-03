package com.e.studentslisttodbapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

    class DbWorker {
    private static final String DATABASE_NAME = "academy.db";
    static final String TABLE = "students";
    private ArrayList<Student> studentsList;

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_F_NAME = "firstNameCol";
    private static final String COLUMN_S_NAME = "secondNameCol";
    private static final String COLUMN_G_NUMBER = "groupNumberCol";
    private static final String COLUMN_AVATAR = "avatarCol";

    private SQLiteDatabase db;
    private Cursor query;
    private Context context;

    DbWorker(Context context) {
        this.context = context;
        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            String queryStr = "select * from students;";
            query = db.rawQuery(queryStr, null);
            if (query != null && query.getCount() > 0) {
                Log.d("ok", "Lines in students table - "+ query.getCount());
            }
        }catch(SQLException sqle){
            fillInDb();
            //throw sqle;
        }
        finally {
            query.close();
        }
    }

    public void fillInDb() {
        studentsList = new ArrayList<>();
        fillStudentsList();

        db.execSQL("CREATE TABLE IF NOT EXISTS '" + TABLE + "'("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_F_NAME + " TEXT, "
                + COLUMN_S_NAME + " TEXT, "
                + COLUMN_G_NUMBER + " TEXT, "
                + COLUMN_AVATAR + " INTEGER);");

        db.beginTransaction();
        try {
            for(int i = 0; i < studentsList.size(); i++){
                Student std = studentsList.get(i);
                int isMale = std.getAvatar() == R.drawable.male_profile ? 1 : 0;
                ContentValues values = new ContentValues();
                values.put(COLUMN_F_NAME,std.getFirstName());
                values.put(COLUMN_S_NAME, std.getSecondName());
                values.put(COLUMN_G_NUMBER, std.getGroupeNumber());
                values.put(COLUMN_AVATAR, isMale);
                db.insert(TABLE, null, values);
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        db.close();
    }

    public StudentAdapter getDataFromDb(){
        ArrayList<Student>students = new ArrayList<>();
        StudentAdapter studentsAdapter;
        try {
            db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        } catch (SQLException sqle) {
            throw sqle;
        }

        query =  db.rawQuery("select * from students;", null);
        if(query.moveToFirst()){
            do{
                int id = query.getInt(0);
                String name = query.getString(1);
                String secondName = query.getString(2);
                String groupName = query.getString(3);
                int studentPicture = query.getInt(4);
                Boolean isMale = studentPicture == 1 ? true : false;
                students.add(new Student(id, name, secondName, groupName, isMale));
            }
            while(query.moveToNext());
        }
        query.close();
        db.close();
        studentsAdapter = new StudentAdapter(context, R.layout.student_list_item, students);
        return studentsAdapter;
    }

    public int insertStudent(Student student) {
        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        int isMale = student.getAvatar() == R.drawable.male_profile ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(COLUMN_F_NAME,student.getFirstName());
        values.put(COLUMN_S_NAME, student.getSecondName());
        values.put(COLUMN_G_NUMBER, student.getGroupeNumber());
        values.put(COLUMN_AVATAR, isMale);
        db.insert(TABLE, null, values);

        Cursor cur = db.rawQuery("SELECT last_insert_rowid()", null);
        cur.moveToFirst();
        int lastId = cur.getInt(0);
        db.close();
        return lastId;
    }

    public void deleteStudents(ArrayList<Integer> list){
        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        if(list.size() == 0) return;
        db.beginTransaction();
        try {
            for(int i = 0; i < list.size(); i++){
                db.delete(TABLE, "_id = ?", new String[]{String.valueOf(list.get(i))});
            }
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        db.close();
    }

    public void updateStudent(Student student){
        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        int isMale = student.getAvatar() == R.drawable.male_profile ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(COLUMN_F_NAME,student.getFirstName());
        values.put(COLUMN_S_NAME, student.getSecondName());
        values.put(COLUMN_G_NUMBER, student.getGroupeNumber());
        values.put(COLUMN_AVATAR, isMale);
        db.update(TABLE, values, COLUMN_ID + "=" + String.valueOf(student.getId()), null);
        db.close();
    }

    public void fillStudentsList(){
        studentsList.add(new Student(-1, "Igor", "Popov", "RT234",true));
        studentsList.add(new Student(-1, "Petr", "Kriptov", "RT234",true));
        studentsList.add(new Student(-1, "Oleg", "Vinnik", "ZT138",true));
        studentsList.add(new Student(-1, "Svetlana", "Kardoba", "RT222",false));
        studentsList.add(new Student(-1, "Olga", "Petrova", "ET210",false));
        studentsList.add(new Student(-1, "Semen", "Ischenko", "KT210",true));
    }
}
