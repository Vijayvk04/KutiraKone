package com.example.kutirakone.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakone.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.ByteArrayOutputStream
import com.google.firebase.auth.FirebaseAuth

class UploadActivity : AppCompatActivity() {

    private lateinit var sizeEt: EditText
    private lateinit var materialEt: EditText
    private lateinit var uploadSaveBtn: Button
    private lateinit var pickImageBtn: Button
    private lateinit var imageView: ImageView

    private var imageBase64: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        sizeEt = findViewById(R.id.sizeEt)
        materialEt = findViewById(R.id.materialEt)
        uploadSaveBtn = findViewById(R.id.uploadSaveBtn)
        pickImageBtn = findViewById(R.id.pickImageBtn)
        imageView = findViewById(R.id.imageView)

        // 👉 Pick Image
        pickImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        // 👉 Upload Data
        uploadSaveBtn.setOnClickListener {

            val size = sizeEt.text.toString().trim()
            val material = materialEt.text.toString().trim()

            if (size.isEmpty() || material.isEmpty() || imageBase64.isEmpty()) {
                Toast.makeText(this, "Fill all fields + image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "testUser"

            val fabric = hashMapOf(
                "size" to size,
                "material" to material,
                "imageUrl" to imageBase64,
                "location" to "Angondhalli",
                "userId" to userId
            )

            db.collection("fabrics")
                .add(fabric)
                .addOnSuccessListener { doc ->
                    Toast.makeText(this, "✅ Uploaded ID: ${doc.id}", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    // 👉 Image Result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data

            if (uri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

                // 🔥 Resize MORE (important)
                val resized = Bitmap.createScaledBitmap(bitmap, 250, 250, true)

                imageView.setImageBitmap(resized)

                // 🔥 Convert to Base64 (compressed)
                imageBase64 = encodeImage(resized)
            }
        }
    }

    // 👉 Convert Image to Base64
    private fun encodeImage(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()

        // 🔥 Reduce quality (VERY IMPORTANT)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream)

        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT)
    }
}