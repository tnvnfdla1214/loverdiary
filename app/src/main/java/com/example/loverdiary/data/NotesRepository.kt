package com.example.loverdiary.data

import com.example.loverdiary.utils.BaseResult
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class NotesRepository : NotesDataSource {

    private val notesCollection =
        FirebaseFirestore.getInstance().collection("notes")

    //처음 실행할 때만
    override fun getAllNotes() = flow<BaseResult<List<Notes>>> {
        emit(BaseResult.loading()) //데이터 전달달
        val snapshot = notesCollection.get().await()
        val notes = snapshot.toObjects(Notes::class.java)
        emit(BaseResult.success(notes))

    }.catch {
        emit(BaseResult.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    // + 메인 어뎁터에 있는 note Arrylist에 추가
    override fun addNotes(notes: Notes) = flow<BaseResult<DocumentReference>> {

        emit(BaseResult.loading())
        val notesRef = notesCollection.add(notes).await()
        emit(BaseResult.success(notesRef))
    }.catch {
        emit(BaseResult.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO )
}