package com.example.loverdiary.Profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.loverdiary.Gallery.GalleryActivity
import com.example.loverdiary.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

    private var SelectedImagePath : String? = null
    private var USER_BirthY = 0
    private var USER_BirthM = 0
    private var USER_BirthD = 0
    val INTENT_PATH = "path"
    private var USER_Gender = 0

    private lateinit var Profile_ImageView : ImageView
    private lateinit var Users_Info_Send_Button : Button
    private lateinit var Nickname_EditText : EditText
    private lateinit var Name_EditText: EditText

    private lateinit var CurrentUser: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val intent = intent
        var gender: String? = null
        gender = intent.getStringExtra("gender")
        var bithday: String? = null
        bithday = intent.getStringExtra("bithday")
        var b_M = 0
        var b_D = 1
        if (bithday != null) {
            val birthday = bithday.toInt()
            b_M = birthday / 100 - 1
            b_D = birthday % 100
        }

        // 현재 사용자를 파이어베이스에서 받아옴
        CurrentUser = FirebaseAuth.getInstance().currentUser

        // 진행중 레이아웃, 프로필 ImageView, 회원정보 등록 Button, 닉네임 EditText find
        Profile_ImageView = findViewById(R.id.Users_Profile_ImageView)
        Users_Info_Send_Button = findViewById(R.id.Users_Info_Send_Button)
        Nickname_EditText = findViewById(R.id.Nickname)
        Name_EditText = findViewById(R.id.Name)

        Profile_ImageView.setOnClickListener(onClickListener)
        Users_Info_Send_Button.setOnClickListener(onClickListener)

        Nickname_EditText.setHint(" " + extractIDFromEmail(CurrentUser.getEmail()))

        val BirthDay_Picker = findViewById<DatePicker>(R.id.BirthDay_Picker)
        USER_BirthY = 2021
        USER_BirthM = b_M
        USER_BirthD = b_D

        BirthDay_Picker.init(2021, b_M, b_D) { view, year, monthOfYear, dayOfMonth ->
            USER_BirthY = year
            USER_BirthM = monthOfYear
            USER_BirthD = dayOfMonth
        }

        // Spinner를 이용해 남,녀 자정

        // Spinner를 이용해 남,녀 자정
        val Gender = findViewById<Spinner>(R.id.Gender)
        val monthAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.Gender,
            android.R.layout.simple_spinner_dropdown_item
        )
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Gender.adapter = monthAdapter

        Gender.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                USER_Gender = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                if (resultCode == RESULT_OK) {
                    SelectedImagePath = data!!.getStringExtra(INTENT_PATH)
                    Glide.with(this).load(SelectedImagePath).centerCrop().override(500)
                        .into(Profile_ImageView)
                }
                if (resultCode == RESULT_CANCELED) {
                    SelectedImagePath = null
                    Glide.with(this).load(R.drawable.non_userprofile_v2).centerInside()
                        .into(Profile_ImageView)
                }
            }
        }
    }
    var onClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.Users_Profile_ImageView -> myStartActivity(GalleryActivity::class.java)
            //R.id.Users_Info_Send_Button ->
                // 스토리지에 사진을 먼저 담는 함수
                //MemberInit_Storage_Uploader()
        }
    }
    fun extractIDFromEmail(email: String): String? {
        val parts = email.split("@".toRegex()).toTypedArray()
        return parts[0]
    }

    private fun myStartActivity(c: Class<*>) {
        val intent = Intent(this, c)
        startActivityForResult(intent, 0)
    }
}