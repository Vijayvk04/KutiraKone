package com.example.kutirakone.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kutirakone.R
import com.example.kutirakone.adapters.FabricAdapter
import com.example.kutirakone.models.FabricModel
import com.google.firebase.firestore.FirebaseFirestore

class FabricListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FabricAdapter
    private lateinit var spinner: Spinner
    private lateinit var searchEt: EditText

    private var originalList = ArrayList<FabricModel>()
    private var displayList = ArrayList<FabricModel>()

    private var selectedMaterial = "All"
    private var searchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fabric_list)

        recyclerView = findViewById(R.id.fabricRecycler)
        spinner = findViewById(R.id.materialSpinner)
        searchEt = findViewById(R.id.searchEt)

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        adapter = FabricAdapter(displayList)
        recyclerView.adapter = adapter

        val db = FirebaseFirestore.getInstance()

        // ✅ REAL TIME FIRESTORE (FIXED)
        db.collection("fabrics")
            .addSnapshotListener { value, error ->

                if (error != null) return@addSnapshotListener

                originalList.clear()

                value?.forEach {
                    originalList.add(it.toObject(FabricModel::class.java))
                }

                applyFilters()
            }

        // ✅ SPINNER
        val materials = arrayOf("All", "Cotton", "Silk", "Wool")
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            materials
        )

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectedMaterial = materials[position]
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ✅ SEARCH
        searchEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchText = s.toString().lowercase()
                applyFilters()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // ✅ MAIN FILTER LOGIC
    private fun applyFilters() {

        displayList.clear()

        for (item in originalList) {

            val matchMaterial =
                selectedMaterial == "All" || item.material.equals(selectedMaterial, true)

            val matchSearch =
                item.material.lowercase().contains(searchText)

            val matchLocation =
                item.location.equals("Angondhalli", true)

            if (matchMaterial && matchSearch && matchLocation) {
                displayList.add(item)
            }
        }

        adapter.notifyDataSetChanged()
    }
}