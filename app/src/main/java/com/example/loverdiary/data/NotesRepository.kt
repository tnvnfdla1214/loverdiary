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

    //Flow 함수 : 비동기 동작의 결과로 suspend function 이 하나이 결과물을 낸다면 플로우를 이용하요 여러개의 원하는 형식으로 던질 수 있다.
    override fun getNotes() = flow<BaseResult<List<Notes>>> {
        emit(BaseResult.loading()) //데이터 전달달
        val snapshot = notesCollection.get().await()
        val notes = snapshot.toObjects(Notes::class.java)
        emit(BaseResult.success(notes))

    }.catch {
        emit(BaseResult.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    override fun addNotes(notes: Notes) = flow<BaseResult<DocumentReference>> {

        emit(BaseResult.loading())
        val notesRef = notesCollection.add(notes).await()
        emit(BaseResult.success(notesRef))
    }.catch {
        emit(BaseResult.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO )
}