package com.example.loverdiary



interface AddNotesContract {
    interface View {
        fun successAddNotes()
        fun failedAddNotes(error: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        suspend fun addNotes(notes: Notes)
    }
}