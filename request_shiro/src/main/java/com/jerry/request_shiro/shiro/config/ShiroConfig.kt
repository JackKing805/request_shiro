package com.jerry.request_shiro.shiro.config

import com.jerry.request_shiro.shiro.impl.ShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCache


data class ShiroConfig(
    val tokenName:String,//token保存的名字
    val validTime:Int,//s
    val cacheType:Class<out IShiroCache> = ShiroCache::class.java,
    val enabledRtLoginVerify:Boolean = false,//是否开启rt协议请求的验证
    val enabledResourcesVerify:Boolean = false,//是否开启资源请求的验证
)