package com.example.testproject

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ClassInfoPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_class_info)

        val list_view: ListView = findViewById(R.id.listView)
        val student_name: EditText = findViewById(R.id.addStudentCell)
        val student_id: EditText = findViewById(R.id.addStudentIDcell)
        val button: Button = findViewById(R.id.addStudentButton)
        val class_title: TextView = findViewById(R.id.class_title)

        class_title.text = intent.getStringExtra("class_name")
        val class_db = DBclass(this, null)
        val returned_bundle: Bundle = class_db.getClassId(class_title.text.toString())
        val class_id: String = returned_bundle.getString("class_id").toString()
        val classes_from_database = fetchDataFromSQLite(class_id)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, classes_from_database)
        list_view.adapter = adapter

        button.setOnClickListener{
            val text = student_name.text.toString().trim() + " " + student_id.text.toString().trim()
            val db = DBstudent(this, null)

            class_db.close()
            db.addStudent(StudentCreator(class_id, student_name.text.toString().trim(), student_id.text.toString().trim()))
            if(text != "")
                adapter.add(text)
        }
        }
    private fun fetchDataFromSQLite(class_title:String): List<String> {

        val db = DBstudent(this, null)
        val readableDB = db.readableDatabase
        val cursor = readableDB.rawQuery("SELECT * FROM students WHERE class_id = '$class_title'", null)

        val items = mutableListOf<String>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("aruco"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                Log.d("Student", "ID: $id, Name: $name")
                items.add(name.toString().trim() + " " + id.toString().trim())
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }



    }
