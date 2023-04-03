package com.jerry.request_shiro.shiro.config

import com.jerry.request_shiro.shiro.impl.ShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCache


data class ShiroConfig(
    val tokenName:String,//token保存的名字
    val validTime:Int,//s
    val cacheType:Class<out IShiroCache> = ShiroCache::class.java,
)