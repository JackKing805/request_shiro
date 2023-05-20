package com.jerry.request_shiro.shiro.config

import com.jerry.request_shiro.shiro.impl.ShiroCache
import com.jerry.request_shiro.shiro.impl.ShiroCacheManager
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.interfaces.IShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCacheManager


data class ShiroConfig(
    val tokenName:String,//token保存的名字
    val validTime:Int = 60,//s
    val authInter:Class<out IShiroAuth> = IShiroAuth::class.java,
    val cacheManagerType:Class<out IShiroCacheManager> = ShiroCacheManager::class.java,
    val cacheType:Class<out IShiroCache> = ShiroCache::class.java,
)