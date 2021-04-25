package com.example.loverdiary.data


import com.example.loverdiary.data.Notes
import com.example.loverdiary.utils.BaseResult
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface NotesDataSource {

    fun getNotes() : Flow<BaseResult<List<Notes>>>

    fun addNotes(notes: Notes) : Flow<BaseResult<DocumentReference>>
}