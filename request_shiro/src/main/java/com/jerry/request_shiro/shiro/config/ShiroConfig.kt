package com.jerry.request_shiro.shiro.config

data class ShiroConfig(
    val tokenName:String,//token保存的名字
    val validTime:Int,//s
)