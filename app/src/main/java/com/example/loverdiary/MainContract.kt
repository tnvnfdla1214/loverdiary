package com.example.loverdiary


interface MainContract {
    interface View {
        fun showNotes(data: List<Notes>)
        fun showLoading()
        fun hideLoading()
        fun failed(error : String)
    }

    interface Presenter {
        suspend fun getAllNotes()
    }
}