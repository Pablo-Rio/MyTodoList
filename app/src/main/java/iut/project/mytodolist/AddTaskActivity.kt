package iut.project.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import iut.project.mytodolist.classes.TaskModelClass
import iut.project.mytodolist.handler.DatabaseHandler

class AddTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)
    }

    private val EMPTY_TEXT = "Nom ou description ne peuvent pas êtres vides"

    //method for saving records in database
    var idCounter = 0
    fun saveRecord(view: View) {
        val name = findViewById<EditText>(R.id.t_name).text.toString()
        val description = findViewById<EditText>(R.id.t_description).text.toString()
        val date = findViewById<EditText>(R.id.t_date).text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (name.trim() != "" && description.trim() != "") {
            val status = databaseHandler.addTask(
                TaskModelClass(
                    idCounter++,
                    name,
                    description,
                    Integer.parseInt(date)
                )
            )
            if (status > -1) {
                Toast.makeText(applicationContext, "Tâche sauvegardée", Toast.LENGTH_LONG).show()
                findViewById<EditText>(R.id.t_name).text.clear()
                findViewById<EditText>(R.id.t_description).text.clear()
                findViewById<EditText>(R.id.t_date).text.clear()

                // Revenir à l'activité principale
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            Toast.makeText(applicationContext, EMPTY_TEXT, Toast.LENGTH_LONG).show()
        }
    }

    fun backOnMainPage(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}