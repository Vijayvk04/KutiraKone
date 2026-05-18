package com.example.kutirakone.adapters

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.kutirakone.R
import com.example.kutirakone.models.FabricModel

class FabricAdapter(private val list: ArrayList<FabricModel>) :
    RecyclerView.Adapter<FabricAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val material: TextView = view.findViewById(R.id.materialTv)
        val size: TextView = view.findViewById(R.id.sizeTv)
        val image: ImageView = view.findViewById(R.id.fabricImage)
        val swapBtn: Button = view.findViewById(R.id.swapBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fabric_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        // ✅ Text binding
        holder.material.text = "Material: ${item.material}"
        holder.size.text = "Size: ${item.size}"

        // ✅ Image decoding (safe)
        try {
            val bytes = Base64.decode(item.imageUrl, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            if (bitmap != null) {
                holder.image.setImageBitmap(bitmap)
            } else {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
            }

        } catch (e: Exception) {
            holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // ✅ Swap button click
        holder.swapBtn.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Swap request sent!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = list.size
}