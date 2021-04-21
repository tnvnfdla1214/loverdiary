package com.example.loverdiary.data.model

import java.util.Date

data class Users(
    var usersName: String? = "",
    var usersGender: String? = "",
    var usersNickName: String? = "",
    var usersBirethdayDate: Date? = Date(),
    var usersCoupleUid: String? = "",
    var usersUid: String? = "",
    var userImage: String? = ""
)
