package com.example.loverdiary.utils

import com.example.loverdiary.DATA_USERS
import com.example.loverdiary.data.model.Users
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AuthManager {

  private val firebaseAuth = FirebaseAuth.getInstance()
  private val firebaseDb = FirebaseFirestore.getInstance()

  fun login(email: String, password: String): Maybe<AuthResult> {
    return Maybe.create<AuthResult> { emitter ->
      RxMaybeHandler.assignOnTask(
        emitter,
        firebaseAuth.signInWithEmailAndPassword(email, password)
      )
    }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  fun register(username: String, email: String, password: String): Maybe<AuthResult> {
    return Maybe.create<AuthResult> { emitter ->
      RxMaybeHandler.assignOnTask(
        emitter,
        firebaseAuth.createUserWithEmailAndPassword(email, password)
      )
    }
      .map {
        val user = Users(username, email, "")
        firebaseDb.collection(DATA_USERS).document(firebaseAuth.uid!!).set(user)
        it
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
  }

  fun isLoggedIn(): Boolean = !firebaseAuth.currentUser?.uid.isNullOrEmpty()

  fun signOut() {
    firebaseAuth.signOut()
  }
}