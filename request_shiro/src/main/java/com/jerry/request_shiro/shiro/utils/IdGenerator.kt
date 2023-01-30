package com.jerry.request_shiro.shiro.utils

import java.util.UUID

internal object IdGenerator {
    fun generatorId(from:String = ""):String{
        return from + UUID.randomUUID().toString()
    }
}