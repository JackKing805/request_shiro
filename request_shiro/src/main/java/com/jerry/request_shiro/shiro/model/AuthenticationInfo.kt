package com.jerry.request_shiro.shiro.model

import androidx.annotation.VisibleForTesting

//认证信息
data class AuthenticationInfo(
    val main:Any,
    val password:Any,
    val token:String
)