package iut.project.mytodolist.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import iut.project.mytodolist.MainActivity
import iut.project.mytodolist.R
import iut.project.mytodolist.classes.TaskModelClass
import iut.project.mytodolist.handler.DatabaseHandler

class MyListAdapter(
    private val context: Activity,
    private val taskId: Array<String>,
    private val taskName: Array<String>,
    private val taskDescription: Array<String>,
    private val taskDate: Array<String>,
    private val taskDone: Array<Int>,
    private val mainActivity: MainActivity
) : ArrayAdapter<String>(context, R.layout.custom_list, taskId) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val boutonDelete = rowView.findViewById(R.id.deleteButton) as Button
        val boutonEdit = rowView.findViewById(R.id.editButton) as Button

        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val nameText = rowView.findViewById(R.id.textViewName) as TextView
        val descriptionText = rowView.findViewById(R.id.textViewDescription) as TextView
        val dateText = rowView.findViewById(R.id.textViewDate) as TextView
        val checkBox = rowView.findViewById(R.id.textViewCheckbox) as CheckBox
        // si taskDone est à 1, on coche la checkbox
        checkBox.isChecked = taskDone[position] == 1

        // Ajout d'un ecouteur sur la checkbox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            taskDone[position] = if (isChecked) 1 else 0
            val dbHandler = DatabaseHandler(context)
            val task = TaskModelClass(
                taskId[position].toInt(),
                taskName[position],
                taskDescription[position],
                taskDate[position],
                taskDone[position]
            )
            dbHandler.updateTask(task)
            mainActivity.viewRecord()
        }

        idText.text = "id: ${taskId[position]}"
        nameText.text = "Titre: ${taskName[position]}"
        descriptionText.text = "Description: ${taskDescription[position]}"
        dateText.text = "Date: ${taskDate[position]}"

        // Liste qui contient l'id, le nom, la description et la date de la tâche
        val taskList = listOf(
            taskId[position],
            taskName[position],
            taskDescription[position],
            taskDate[position],
            taskDone[position]
        )

        boutonDelete.contentDescription = taskId[position]
        boutonEdit.contentDescription = taskList.toString()

        return rowView
    }
}