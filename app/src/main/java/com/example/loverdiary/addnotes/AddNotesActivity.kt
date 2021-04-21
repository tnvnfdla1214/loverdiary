package com.example.loverdiary.addnotes

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loverdiary.R
import com.example.loverdiary.data.Notes
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddNotesActivity : AppCompatActivity(),
    AddNotesContract.View {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AddNotesActivity::class.java))
        }
    }
//zz
    private lateinit var presenter: AddNotesPresenter

    private lateinit var dialog: AlertDialog

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        actionBar?.setDisplayHomeAsUpEnabled(true)
        if (supportActionBar != null) {
            supportActionBar?.title = getString(R.string.action_add_notes)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)

        builder.setView(R.layout.layout_loading_dialog)
        dialog = builder.create()

        presenter = AddNotesPresenter(this)

        btnAddNotes.setOnClickListener {
            uiScope.launch {
                addNotes()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun addNotes() {
        val title = edtTitle.text.toString().trim()
        val message = edtMessage.text.toString().trim()
        val id = UUID.randomUUID().toString()
        val dateAndtime: LocalDateTime = LocalDateTime.now()
        val dateTime = dateAndtime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        val notes = Notes(title, message, 0,id,dateTime)
        presenter.addNotes(notes)
    }

    override fun successAddNotes(notes: Notes) {
        val intent = Intent()
        intent.putExtra("notes", notes)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun failedAddNotes(error: String) {
        Toast.makeText(this, "Failed to Send Data!", Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        dialog.show()
    }

    override fun hideLoading() {
        dialog.dismiss()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}