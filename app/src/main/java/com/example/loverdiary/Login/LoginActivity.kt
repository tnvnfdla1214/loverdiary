package com.example.loverdiary.Login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loverdiary.R
import com.example.loverdiary.ui.Profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var kakao_login_button : ImageButton
    private var fireBaseAuth : FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fireBaseAuth = FirebaseAuth.getInstance()

        kakao_login_button = findViewById(R.id.kakao_login_button)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else if (token != null) {
                getData()

            }
        }

        kakao_login_button.setOnClickListener {
                //에러가 존재할 경우(로그인 정보 없음) -> 로그인 시도
                    LoginClient.instance.run {
                        if(isKakaoTalkLoginAvailable(this@LoginActivity)){
                            loginWithKakaoTalk(this@LoginActivity,callback = callback)
                        }else {
                            loginWithKakaoAccount(this@LoginActivity, callback = callback)
                        }
                    }
        }
    }
    fun getData() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.d("Hash", "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                val email = user.kakaoAccount?.email
                val gender = user.kakaoAccount?.gender
                val birthday = user.kakaoAccount?.birthday
                val password = "1234567890"
                firebaseAuthKakaoLogin(email!!, password)
                Log.d("Hash", "email " + email)         // 이메일
                Log.d("Hash", "gender " + gender)       // MALE
                Log.d("Hash", "birthday " + birthday)   // 년도는 못받음
            }
        }
    }
    fun firebaseAuthKakaoSignup(email : String, password : String){
        fireBaseAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(!it.isSuccessful){
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
    }
    fun firebaseAuthKakaoLogin(email : String, password : String){
        fireBaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this, "로그인 성공하였습니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }else{
                    Toast.makeText(this, "회원가입 하고 오세요.", Toast.LENGTH_SHORT).show()
                    firebaseAuthKakaoSignup(email, password)
                }
            }
    }
}