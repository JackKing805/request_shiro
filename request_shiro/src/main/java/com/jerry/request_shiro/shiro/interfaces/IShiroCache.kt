package com.jerry.request_shiro.shiro.interfaces

import java.util.Date

open class IShiroCache {
    private val map = mutableMapOf<String, Any?>()
    private val createTime = System.currentTimeMillis()
    private var expiresTime: Date? = null
    private var maxAge = -1L
    private var id: String = ""

    open fun setId(id:String){
        this.id = id
    }

    open fun getID():String = id

    //设置到期时间
    open fun setExpires(date: Date){
        expiresTime = date
    }

    //设置从当前时间开始最长存活时间
    open fun setMaxAge(mill:Long){
        maxAge = mill
    }

    open fun isValid():Boolean{
        if (expiresTime != null) {
            val now = Date()
            val result = now.compareTo(expiresTime!!)
            if (result == -1) {
                return false
            }
        }

        if (maxAge != -1L) {
            val now = System.currentTimeMillis()
            if (createTime + maxAge < now) {
                return false
            }
        }

        return true
    }

    open fun createTime():Long{
        return createTime
    }

    open fun getValue(key:String,defaultValue:Any?):Any?{
        return map[key] ?: defaultValue
    }

    open fun putValue(key: String,value:Any?){
        map[key] = value
    }
}