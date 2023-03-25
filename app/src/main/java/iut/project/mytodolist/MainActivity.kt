package iut.project.mytodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.content.DialogInterface
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import iut.project.mytodolist.adapter.MyListAdapter
import iut.project.mytodolist.classes.TaskModelClass
import iut.project.mytodolist.handler.DatabaseHandler


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    private val EMPTY_TEXT = "Nom ou description ne peuvent pas êtres vides"
    //method for saving records in database
    var idCounter = 0
    fun saveRecord(view: View){
        val id = findViewById<EditText>(R.id.t_id).text.toString()
        val name = findViewById<EditText>(R.id.t_name).text.toString()
        val description = findViewById<EditText>(R.id.t_description).text.toString()
        val date = findViewById<EditText>(R.id.t_date).text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(name.trim()!="" && description.trim()!=""){
            val status = databaseHandler.addTask(
                TaskModelClass(
                    idCounter++,
                    name,
                    description,
                    Integer.parseInt(date)
                )
            )
            if(status > -1){
                Toast.makeText(applicationContext,"Tâche sauvegardée",Toast.LENGTH_LONG).show()
                findViewById<EditText>(R.id.t_id).text.clear()
                findViewById<EditText>(R.id.t_name).text.clear()
                findViewById<EditText>(R.id.t_description).text.clear()
                findViewById<EditText>(R.id.t_date).text.clear()
            }
        }else{
            Toast.makeText(applicationContext,EMPTY_TEXT,Toast.LENGTH_LONG).show()
        }

    }
    //method for read records from database in ListView
    fun viewRecord(view: View){
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val task: List<TaskModelClass> = databaseHandler.viewTask()
        val taskArrayId = Array<String>(task.size){"0"}
        val taskArrayName = Array<String>(task.size){"null"}
        val taskArrayDescription = Array<String>(task.size){"null"}
        val taskArrayDate = Array<String>(task.size){"0"}
        var index = 0
        for(tas in task){
            taskArrayId[index] = tas.taskId.toString()
            taskArrayName[index] = tas.taskName
            taskArrayDescription[index] = tas.taskDescription
            taskArrayDate[index] = tas.taskDate.toString()
            index++
        }
        //creating custom ArrayAdapter
        val myListAdapter = MyListAdapter(
            this,
            taskArrayId,
            taskArrayName,
            taskArrayDescription,
            taskArrayDate
        )
        findViewById<ListView>(R.id.listView).adapter = myListAdapter
    }
    //method for updating records based on user id
    fun updateRecord(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtDescription = dialogView.findViewById(R.id.updateDescription) as EditText
        val edtDate = dialogView.findViewById(R.id.updateDate) as EditText

        dialogBuilder.setTitle("Update Record")
        dialogBuilder.setMessage("Enter data below")
        dialogBuilder.setPositiveButton("Update", DialogInterface.OnClickListener { _, _ ->

            val updateId = edtId.text.toString()
            val updateName = edtName.text.toString()
            val updateDescription = edtDescription.text.toString()
            val updateDate = edtDate.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler= DatabaseHandler(this)
            if(updateId.trim()!="" && updateName.trim()!="" && updateDescription.trim()!=""){
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateTask(TaskModelClass(Integer.parseInt(updateId),updateName, updateDescription, Integer.parseInt(updateDate)))
                if(status > -1){
                    Toast.makeText(applicationContext,"record update",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,EMPTY_TEXT,Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
    //method for deleting records based on id
    fun deleteRecord(view: View){
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter id below")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if(deleteId.trim()!=""){
                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteTask(TaskModelClass(Integer.parseInt(deleteId),"","",0))
                if(status > -1){
                    Toast.makeText(applicationContext,"record deleted",Toast.LENGTH_LONG).show()
                }
            }else{
                Toast.makeText(applicationContext,EMPTY_TEXT,Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }
}