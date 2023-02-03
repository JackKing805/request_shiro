package com.jerry.request_shiro.main.request

import android.util.Log
import com.jerry.request_core.anno.ExceptionHandler
import com.jerry.request_core.anno.ExceptionRule
import com.jerry.request_shiro.shiro.exception.ShiroAuthException

@ExceptionRule
class ShiroExceptionHandler {
    @ExceptionHandler(ShiroAuthException::class)
    fun onShiroException(e:ShiroAuthException):String{
        Log.e("ADSAD","onShiroException:${e}")
        return "No Role Or Permission"
    }

    @ExceptionHandler(Exception::class)
    fun onE(e:Exception):String{
        e.printStackTrace()
        return e.toString()
    }
}