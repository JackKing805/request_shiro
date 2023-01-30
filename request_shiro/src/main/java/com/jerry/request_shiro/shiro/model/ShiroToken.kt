package com.jerry.request_shiro.shiro.model

data class ShiroToken (
    val authorizationInfo: AuthorizationInfo,
    val authenticationInfo: AuthenticationInfo
        )