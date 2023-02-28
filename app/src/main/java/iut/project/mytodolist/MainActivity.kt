package iut.project.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnTache = findViewById<Button>(R.id.btnTache)
        btnTache.setOnClickListener {
            val intent = Intent(this@MainActivity, CreationTache::class.java)
            startActivity(intent)
        }
    }
}