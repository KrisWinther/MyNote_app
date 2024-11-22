package com.example.mynote1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class read:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read)
        val back: TextView = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val textmain: TextView = findViewById(R.id.textmain)
        val textdesc: TextView = findViewById(R.id.textdesc)
        val id = intent.getStringExtra("id").toString()
        val db = Firebase.firestore
        db.collection("notes").document(id).get().addOnSuccessListener {
            textmain.text = it.getString("name").toString()
            textdesc.text = it.getString("desctext").toString()
        }.addOnFailureListener {
            Toast.makeText(this, "Не удалось получить данные.", Toast.LENGTH_SHORT).show()
        }
        val updButton: TextView = findViewById(R.id.updater)
        updButton.setOnClickListener {
            val intent = Intent(this, update::class.java)
                .putExtra("noteId", id)
            startActivity(intent)}

        val delite: TextView = findViewById(R.id.deliter)
        delite.setOnClickListener {
            db.collection("notes").document(id)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Успешно удалено.", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
                .addOnFailureListener { _ ->
                    Toast.makeText(this, "Не удалось удалить...", Toast.LENGTH_SHORT).show()}
        }
        textmain.setOnLongClickListener {
            copyToClipboard(textmain.text.toString())
            true
        }

        textdesc.setOnLongClickListener {
            copyToClipboard(textdesc.text.toString())
            true
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Текст скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
    }
}