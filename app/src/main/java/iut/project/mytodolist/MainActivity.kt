package iut.project.mytodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
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

    override fun onStart() {
        super.onStart()
        viewRecord(this.findViewById(R.id.listView))
    }

    private val EMPTY_TEXT = "Nom ou description ne peuvent pas êtres vides"

    //method for read records from database in ListView
    fun viewRecord(view: View) {
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val task: List<TaskModelClass> = databaseHandler.viewTask()
        val taskArrayId = Array<String>(task.size) { "0" }
        val taskArrayName = Array<String>(task.size) { "null" }
        val taskArrayDescription = Array<String>(task.size) { "null" }
        val taskArrayDate = Array<String>(task.size) { "0" }
        var index = 0
        for (tas in task) {
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
    fun updateRecord(view: View) {
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
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (updateId.trim() != "" && updateName.trim() != "" && updateDescription.trim() != "") {
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateTask(
                    TaskModelClass(
                        Integer.parseInt(updateId),
                        updateName,
                        updateDescription,
                        Integer.parseInt(updateDate)
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "record update", Toast.LENGTH_LONG).show()
                    viewRecord(this.findViewById(R.id.listView))
                }
            } else {
                Toast.makeText(applicationContext, EMPTY_TEXT, Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }

    //method for deleting records based on id
    fun deleteRecord(view: View) {
        // L'id du bouton à supprimer
        // Le résultat de bouton.contentDescription est "id: 1" par exemple, on veut juste le 1
        val id = view.contentDescription.split(" ")[1]
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.deleteTask(
            TaskModelClass(
                Integer.parseInt(id),
                "",
                "",
                0
            )
        )
        if (status > -1) {
            Toast.makeText(applicationContext, "record deleted", Toast.LENGTH_LONG).show()
            viewRecord(this.findViewById(R.id.listView))
        }
    }

    fun addTaskPage(view: View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }
}