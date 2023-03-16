package iut.project.mytodolist.handler

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import iut.project.mytodolist.classes.TaskModelClass

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskDatabase"
        private const val TABLE_TASKS = "TaskTable"
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_DESCRIPTION = "description"
        private const val KEY_DATE = "date"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_TASKS_TABLE = ("CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT" + KEY_DATE + " INTEGER " + ")")
        db?.execSQL(CREATE_TASKS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS)
        onCreate(db)
    }


    //method to insert data
    fun addTask(t: TaskModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, t.taskId)
        contentValues.put(KEY_NAME, t.taskName)
        contentValues.put(KEY_DESCRIPTION,t.taskDescription )
        contentValues.put(KEY_DATE, t.taskDate)
        // Inserting Row
        val success = db.insert(TABLE_TASKS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewTasks():List<TaskModelClass>{
        val tasList:ArrayList<TaskModelClass> = ArrayList<TaskModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_TASKS"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var taskId: Int
        var taskName: String
        var taskDescription: String
        var taskDate: Int
        if (cursor.moveToFirst()) {
            do {
                taskId = cursor.getInt(cursor.getColumnIndex("id"))
                taskName = cursor.getString(cursor.getColumnIndex("name"))
                taskDescription = cursor.getString(cursor.getColumnIndex("description"))
                taskDate = cursor.getInt(cursor.getColumnIndex("date"))
                val tas= TaskModelClass(taskId = taskId, taskName = taskName, taskDescription = taskDescription, taskDate = taskDate)
                tasList.add(tas)
            } while (cursor.moveToNext())
        }
        return tasList
    }
    //method to update data
    fun updateTask(tas: TaskModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, tas.taskId)
        contentValues.put(KEY_NAME, tas.taskName)
        contentValues.put(KEY_DESCRIPTION,tas.taskDescription )
        contentValues.put(KEY_DATE, tas.taskDate)

        // Updating Row
        val success = db.update(TABLE_TASKS, contentValues,"id="+tas.taskId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteTask(tas: TaskModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, tas.taskId)
        // Deleting Row
        val success = db.delete(TABLE_TASKS,"id="+tas.taskId,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}