package com.jerry.request_shiro.shiro.model

import androidx.annotation.VisibleForTesting
import java.io.Serializable

//认证信息
data class AuthenticationInfo(
    val main:Any,
    val password:Any,
    val token:String
):Serializable