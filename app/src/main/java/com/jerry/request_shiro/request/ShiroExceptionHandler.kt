package com.jerry.request_shiro.request

import android.util.Log
import com.jerry.request_core.anno.ExceptionHandler
import com.jerry.request_core.anno.ExceptionRule
import com.jerry.request_shiro.shiro.exception.ShiroException

@ExceptionRule
class ShiroExceptionHandler {
    @ExceptionHandler(ShiroException::class)
    fun onShiroException(e:ShiroException){
        Log.e("ADSAD","onShiroException:${e}")

    }
}