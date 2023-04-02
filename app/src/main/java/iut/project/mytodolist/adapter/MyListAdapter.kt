package iut.project.mytodolist.adapter

import android.app.Activity
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import iut.project.mytodolist.MainActivity
import iut.project.mytodolist.R
import iut.project.mytodolist.classes.TaskModelClass
import iut.project.mytodolist.handler.DatabaseHandler
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MyListAdapter(
    private val context: Activity,
    private val taskId: Array<String>,
    private val taskName: Array<String>,
    private val taskDescription: Array<String>,
    private val taskDate: Array<String>,
    private val taskDone: Array<Int>,
    private val mainActivity: MainActivity
) : ArrayAdapter<String>(context, R.layout.custom_list, taskId) {

    var checkBox: CheckBox? = null

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val boutonDelete = rowView.findViewById(R.id.deleteButton) as ImageView
        val boutonEdit = rowView.findViewById(R.id.editButton) as ImageView


        if (isDarkTheme()) {
            val color = ContextCompat.getColor(mainActivity, R.color.imageSombre)
            boutonEdit.setColorFilter(color)
        } else {
            val color = ContextCompat.getColor(mainActivity, R.color.imageClair)
            boutonEdit.setColorFilter(color)
        }

        val title = rowView.findViewById(R.id.textTitle) as TextView
        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val nameText = rowView.findViewById(R.id.textViewName) as TextView
        val descriptionText = rowView.findViewById(R.id.textViewDescription) as TextView
        val dateText = rowView.findViewById(R.id.textViewDate) as TextView
        checkBox = rowView.findViewById(R.id.textViewCheckbox) as CheckBox
        // si taskDone est à 1, on coche la checkbox
        checkBox!!.isChecked = taskDone[position] == 1

        val party = Party(
            speed = 0f,
            maxSpeed = 35f,
            damping = 0.9f,
            spread = 600,
            colors = listOf(0x73D5FF, 0xED6923, 0xB8EAFF, 0xFFDD00),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
        val konffeti = mainActivity.findViewById<KonfettiView>(R.id.konfettiView)

        // Ajout d'un ecouteur sur la checkbox
        checkBox!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                taskDone[position] = 1
                konffeti.start(party)
            } else {
                taskDone[position] = 0
            }
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

        dateText.visibility = View.GONE
        if (taskDate[position] == mainActivity.yesterdayDate) {
            title.text = "Hier"
        } else if (taskDate[position] == mainActivity.todayDate) {
            title.text = "Aujourd'hui"
        } else if (taskDate[position] == mainActivity.tomorrowDate) {
            title.text = "Demain"
        } else if (taskDate[position] == "") {
            title.text = "Sans date"
        } else {
            title.text = mainActivity.formatDate(taskDate[position])
        }

        // Si le titre de la tache précédente est différent du titre de la tache actuelle, on affiche le titre
        if (position > 0) {
            if (taskDate[position - 1] != taskDate[position]) {
                title.visibility = View.VISIBLE
            } else {
                title.visibility = View.GONE
            }
        }

        idText.text = "id: ${taskId[position]}"
        nameText.text = "${taskName[position]}"
        descriptionText.text = "${taskDescription[position]}"
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

    fun isDarkTheme(): Boolean {
        val nightMode = mainActivity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightMode == Configuration.UI_MODE_NIGHT_YES
    }
}