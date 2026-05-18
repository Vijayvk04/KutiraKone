package com.example.kutirakone.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ FIRST: load layout
        setContentView(R.layout.activity_main)

        // ✅ THEN: find buttons
        val uploadBtn = findViewById<Button>(R.id.uploadBtn)
        val viewBtn = findViewById<Button>(R.id.viewBtn)
        val ideasBtn = findViewById<Button>(R.id.ideasBtn)

        // ✅ Click actions
        uploadBtn.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        viewBtn.setOnClickListener {
            startActivity(Intent(this, FabricListActivity::class.java))
        }

        // ✅ ADD YOUR CODE HERE
        ideasBtn.setOnClickListener {
            Toast.makeText(
                this,
                "Ideas: Masks, Bags, Cushion Covers",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}