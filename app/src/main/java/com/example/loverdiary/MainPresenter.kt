package com.example.loverdiary

import kotlinx.coroutines.flow.collect

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private var repository: NotesRepository = NotesRepository()

     override suspend fun getAllNotes() {
         repository.getNotes().collect { state ->
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