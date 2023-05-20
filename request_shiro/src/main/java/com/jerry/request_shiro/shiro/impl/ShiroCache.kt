package com.jerry.request_shiro.shiro.impl

import com.drake.serialize.serialize.serial
import com.drake.serialize.serialize.serialize
import com.jerry.request_shiro.shiro.interfaces.IShiroCache
import java.util.Date

class ShiroCache : IShiroCache(){
    override fun getValue(key: String, defaultValue: Any?): Any? {
        return if (isValid()){
            serial(key)
        }else{
            serialize(key to null)
            null
        }
    }

    override fun putValue(key: String, value: Any?) {
        serialize(key to value)
    }
}