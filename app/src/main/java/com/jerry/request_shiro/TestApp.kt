package com.jerry.request_shiro

import android.app.Application
import com.jerry.request_core.Core
import com.jerry.request_shiro.request.Config
import com.jerry.request_shiro.request.ShiroExceptionHandler
import com.jerry.request_shiro.request.TestController
import com.jerry.request_shiro.shiro.ShiroUtils

class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val toMutableList = ShiroUtils.getConfigurationList().toMutableList()
        toMutableList.add(TestController::class.java)
        toMutableList.add(Config::class.java)
        toMutableList.add(ShiroExceptionHandler::class.java)
        Core.init(this, toMutableList)
    }
}