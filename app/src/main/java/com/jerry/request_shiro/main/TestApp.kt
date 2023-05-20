package com.jerry.request_shiro.main

import android.app.Application
import com.jerry.request_core.Core
import com.jerry.request_shiro.main.request.Config
import com.jerry.request_shiro.main.request.ShiroExceptionHandler
import com.jerry.request_shiro.main.request.TestController
import com.jerry.request_shiro.main.request.naw.AB
import com.tencent.mmkv.MMKV

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)

        val toMutableList = mutableListOf<Class<*>>()
        toMutableList.add(TestController::class.java)
        toMutableList.add(Config::class.java)
        toMutableList.add(ShiroExceptionHandler::class.java)
        Core.init(this, toMutableList)
    }
}