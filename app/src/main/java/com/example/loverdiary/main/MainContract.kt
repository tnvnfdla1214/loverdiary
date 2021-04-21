package com.example.loverdiary.main

import android.content.Intent
import com.example.loverdiary.data.Notes

interface MainContract {
    interface View {
        fun showNotes(data: List<Notes>)
        fun showLoading()
        fun hideLoading()
        fun failed(error : String)
        fun addNoteResult()
    }

    interface Presenter {
        suspend fun getAllNotes()
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }
}