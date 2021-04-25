package com.example.loverdiary


import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface NotesDataSource {

    fun getNotes() : Flow<BaseResult<List<Notes>>>

    fun addNotes(notes: Notes) : Flow<BaseResult<DocumentReference>>
}