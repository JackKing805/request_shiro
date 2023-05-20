package com.jerry.request_shiro.main.request.naw

import android.util.Log
import com.drake.serialize.serialize.serial
import com.drake.serialize.serialize.serialize
import com.jerry.request_base.annotations.Bean
import com.jerry.request_shiro.shiro.interfaces.IShiroCache

class AB: IShiroCache() {
    override fun getValue(key: String, defaultValue: Any?): Any? {
        Log.e("ASDASD","abget")
        return if (isValid()){
            serial(key)
        }else{
            serialize(key to null)
            null
        }
    }

    override fun putValue(key: String, value: Any?) {
        Log.e("ASDASD","abput")
        serialize(key to value)
    }
}