package com.example.loverdiary.addnotes

import com.example.loverdiary.data.Notes

interface AddNotesContract {
    interface View {
        fun successAddNotes(notes: Notes)
        fun failedAddNotes(error: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        suspend fun addNotes(notes: Notes)
    }
}