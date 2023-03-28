package iut.project.mytodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.content.DialogInterface
import android.content.Intent
import android.text.Editable
import android.widget.DatePicker
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import iut.project.mytodolist.adapter.MyListAdapter
import iut.project.mytodolist.classes.TaskModelClass
import iut.project.mytodolist.handler.DatabaseHandler
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Locale.setDefault(Locale.FRANCE)
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
    //method for updating records based on user id
    fun updateRecord(view: View) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)

        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        val edtDescription = dialogView.findViewById(R.id.updateDescription) as EditText
        val edtDate = dialogView.findViewById(R.id.updateDate) as DatePicker

        val id = view.contentDescription.toString().split(",")[0]

        val name = view.contentDescription.toString().split(",")[1]
        edtName.text = Editable.Factory.getInstance().newEditable(name.substring(1))

        val description = view.contentDescription.toString().split(",")[2]
        edtDescription.text = Editable.Factory.getInstance().newEditable(description.substring(1))

        val date = view.contentDescription.toString().split(",")[3].substring(1, view.contentDescription.toString().split(",")[3].length - 1)
        val day = date.split("/")[0].toInt()
        val month = date.split("/")[1].toInt() - 1
        val year = date.split("/")[2].toInt()


        edtDate.init(year, month, day, null)

        dialogBuilder.setTitle("Édition de la tâche")
        dialogBuilder.setMessage("Modifier les champs souhaités")
        dialogBuilder.setPositiveButton("Modifier", DialogInterface.OnClickListener { _, _ ->

            val updateName = edtName.text.toString()
            val updateDescription = edtDescription.text.toString()
            val updateDay = edtDate.dayOfMonth
            val updateMonth = edtDate.month + 1
            val updateYear = edtDate.year

            val updateDate = if (updateMonth < 10) {
                "$updateDay/0$updateMonth/$updateYear"
            }  else {
                "$updateDay/$updateMonth/$updateYear"
            }

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (updateName.trim() != "" && updateDescription.trim() != "") {
                //calling the updateEmployee method of DatabaseHandler class to update record
                val status = databaseHandler.updateTask(
                    TaskModelClass(
                        Integer.parseInt(id.substring(1, id.length)),
                        updateName,
                        updateDescription,
                        updateDate
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "Modification réussie", Toast.LENGTH_LONG)
                        .show()
                    viewRecord(this.findViewById(R.id.listView))
                }
            } else {
                Toast.makeText(applicationContext, EMPTY_TEXT, Toast.LENGTH_LONG).show()
            }

        })
        dialogBuilder.setNegativeButton("Annuler", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }


    //method for deleting records based on id
    fun deleteRecord(view: View) {
        val id = view.contentDescription.toString()
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val status = databaseHandler.deleteTask(
            TaskModelClass(
                Integer.parseInt(id),
                "",
                "",
                ""
            )
        )
        if (status > -1) {
            Toast.makeText(applicationContext, "Tâche terminée, Bravo !", Toast.LENGTH_LONG).show()
            viewRecord(this.findViewById(R.id.listView))
        }
    }

    fun addTaskPage(view: View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivity(intent)
    }
}