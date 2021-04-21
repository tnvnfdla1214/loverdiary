package com.example.loverdiary.main

import com.example.loverdiary.utils.BaseResult
import com.example.loverdiary.data.NotesRepository
import kotlinx.coroutines.flow.collect

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

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
}