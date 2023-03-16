package iut.project.mytodolist.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import iut.project.mytodolist.R

class MyListAdapter(private val context: Activity, private val id: Array<String>, private val name: Array<String>, private val description: Array<String>, private val date: Array<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, name) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val idText = rowView.findViewById(R.id.textViewId) as TextView
        val nameText = rowView.findViewById(R.id.textViewName) as TextView
        val descriptionText = rowView.findViewById(R.id.textViewDescription) as TextView
        val dateText = rowView.findViewById(R.id.textViewDate) as TextView

        idText.text = "Id: ${id[position]}"
        nameText.text = "Name: ${name[position]}"
        descriptionText.text = "Description: ${description[position]}"
        dateText.text = "Date: ${date[position]}"
        return rowView
    }
}