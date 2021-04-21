package com.example.loverdiary.main

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.loverdiary.data.Notes
import com.example.loverdiary.utils.BaseResult
import com.example.loverdiary.data.NotesRepository
import kotlinx.coroutines.flow.collect

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private val mainAdapter: MainAdapter by lazy {
        MainAdapter()
    }
    private var repository: NotesRepository =
        NotesRepository()

     override suspend fun getAllNotes() {
         repository.getAllNotes().collect { state ->
             when (state) {
                 is BaseResult.Loading -> {
                     view.showLoading()
                 }

                 is BaseResult.Success -> {
                     view.hideLoading()
                     view.showNotes(state.data)
                 }

                 is BaseResult.Failed -> {
                     view.hideLoading()
                     view.failed(state.message)
                 }
             }
         }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode) {
            100 -> {
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        var noteList = ArrayList<Notes>()
                        noteList = mainAdapter.getNotesList()!!
                        val noteData = data?.getParcelableExtra<Notes>("notes")
                        noteList.add(noteData!!)
                        Log.d("test","1")
                        view.showNotes(noteList)
                    }
                }
            }
        }

    }
}