package iut.project.mytodolist.handler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import iut.project.mytodolist.classes.TaskModelClass

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "TaskDatabase"
        private val TABLE_TASKS = "TaskTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_DESCRIPTION = "description"
        private val KEY_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val CREATE_TASKS_TABLE = ("CREATE TABLE $TABLE_TASKS ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_NAME TEXT, "
                + "$KEY_DESCRIPTION TEXT, "
                + "$KEY_DATE TEXT)")
        db?.execSQL(CREATE_TASKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS)
        onCreate(db)
    }

    //method to insert data
    fun addTask(task: TaskModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, task.taskName) // TaskModelClass taskName
        contentValues.put(KEY_DESCRIPTION, task.taskDescription) // TaskModelClass taskDescription
        contentValues.put(KEY_DATE, task.taskDate) // TaskModelClass taskDate
        // Inserting Row
        val success = db.insert(TABLE_TASKS, null, contentValues)
        db.close() // Closing database connection
        return success
    }

    //method to read data
    fun viewTask(): List<TaskModelClass> {
        val taskList: ArrayList<TaskModelClass> = ArrayList<TaskModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var taskId: Int
        var taskName: String
        var taskDescription: String
        var taskDate: String
        if (cursor.moveToFirst()) {
            do {
                taskId = cursor.getInt(cursor.getColumnIndex("id"))
                taskName = cursor.getString(cursor.getColumnIndex("name"))
                taskDescription = cursor.getString(cursor.getColumnIndex("description"))
                taskDate = cursor.getString(cursor.getColumnIndex("date"))
                val task = TaskModelClass(
                    taskId = taskId,
                    taskName = taskName,
                    taskDescription = taskDescription,
                    taskDate = taskDate
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        return taskList
    }

    //method to update data
    fun updateTask(task: TaskModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.taskId) // TaskModelClass taskId
        contentValues.put(KEY_NAME, task.taskName) // TaskModelClass taskName
        contentValues.put(KEY_DESCRIPTION, task.taskDescription) // TaskModelClass taskDescription
        contentValues.put(KEY_DATE, task.taskDate) // TaskModelClass taskDate
        // Updating Row
        val success = db.update(TABLE_TASKS, contentValues, "id=" + task.taskId, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to delete data
    fun deleteTask(task: TaskModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.taskId) // TaskModelClass taskId
        // Deleting Row
        val success = db.delete(TABLE_TASKS, "id=" + task.taskId, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}