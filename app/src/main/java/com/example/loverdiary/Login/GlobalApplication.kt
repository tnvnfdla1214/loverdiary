package com.example.loverdiary.Login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "0bf0c5a07430d2db413a76aca56fa803")
    }

}