package com.jerry.request_shiro.shiro.model

import java.io.Serializable

data class ShiroInfo (
    val authorizationInfo: AuthorizationInfo,
    val authenticationInfo: AuthenticationInfo):Serializable