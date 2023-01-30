package com.jerry.request_shiro.shiro.interfaces

import com.jerry.request_shiro.shiro.model.AuthToken
import com.jerry.request_shiro.shiro.model.AuthenticationInfo
import com.jerry.request_shiro.shiro.model.AuthorizationInfo


interface IShiroAuth {
    //认证,返回token
    fun onAuthentication(authToken: AuthToken): AuthenticationInfo

    //授权
    fun onAuthorization(authorization: AuthenticationInfo):AuthorizationInfo
}
