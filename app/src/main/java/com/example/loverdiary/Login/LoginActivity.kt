package com.example.loverdiary.Login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.loverdiary.Profile.ProfileActivity
import com.example.loverdiary.R
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var kakao_login_button : ImageButton
    private lateinit var google_login_button : ImageButton
    private var fireBaseAuth : FirebaseAuth? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val GOOGLE_LOGIN_CODE  = 9001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fireBaseAuth = FirebaseAuth.getInstance()
        kakao_login_button = findViewById(R.id.kakao_login_button)
        google_login_button = findViewById(R.id.google_login_button)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
            }
            else if (token != null) {
                getData()

            }
        }

        kakao_login_button.setOnClickListener {
                //에러가 존재할 경우(로그인 정보 없음) -> 로그인 시도
                    LoginClient.instance.run {
                        if(isKakaoTalkLoginAvailable(this@LoginActivity)){
                            loginWithKakaoTalk(this@LoginActivity, callback = callback)
                        }else {
                            loginWithKakaoAccount(this@LoginActivity, callback = callback)
                        }
                    }
        }

        //구글 로그인 구성
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) .requestEmail()
            .build()
        //구글 로그인 관리 클래스
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        google_login_button.setOnClickListener {
            googleLogin()
        }


    }
    fun googleLogin() {
        var signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //구글 승인 정보 가져오기
        if (requestCode == GOOGLE_LOGIN_CODE  && resultCode == Activity.RESULT_OK) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            println(result.status.toString())
            if (result.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {

            }
        }
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        fireBaseAuth!!.signInWithCredential(credential).addOnCompleteListener { task -> if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                val user = fireBaseAuth!!.currentUser
                moveMain(user)
                } else {
                    val loginfailalert = AlertDialog.Builder(this)
                    loginfailalert.setMessage("잠시 후 다시 시도해주세요.")
                    loginfailalert.setPositiveButton("확인", null)
                    loginfailalert.show()
                    moveMain(null)
                }
        }
    }
    public override fun onStart() {
        super.onStart()
        //유저가 로그인되어있는지 확인
        val currentUser = fireBaseAuth!!.currentUser
        moveMain(currentUser)
    }
    //유저가 로그인하면 메인액티비티로 이동
    private fun moveMain(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }




    fun getData() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("Hash", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                firebaseAuthKakaoLogin(user)
            }
        }
    }

    fun firebaseAuthKakaoSignup(user: User){
        val email = user.kakaoAccount?.email
        val password = "1234567890"
        val gender = user.kakaoAccount?.gender
        val birthday = user.kakaoAccount?.birthday

        fireBaseAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(!it.isSuccessful){
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("gender", gender)
                    intent.putExtra("bithday", birthday)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
    }
    fun firebaseAuthKakaoLogin(user: User){
        val email = user.kakaoAccount?.email
        val password = "1234567890"
        val gender = user.kakaoAccount?.gender
        val birthday = user.kakaoAccount?.birthday

        fireBaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "로그인 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("gender", gender)
                    intent.putExtra("bithday", birthday)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }else{
                    Toast.makeText(this, "회원가입 하고 오세요.", Toast.LENGTH_SHORT).show()
                    firebaseAuthKakaoSignup(user)
                }
            }
    }
}