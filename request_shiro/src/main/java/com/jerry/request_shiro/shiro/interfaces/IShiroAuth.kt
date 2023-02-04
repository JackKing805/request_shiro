package com.jerry.request_shiro.shiro.interfaces

import com.jerry.request_shiro.shiro.ShiroUtils
import com.jerry.request_shiro.shiro.model.AuthToken
import com.jerry.request_shiro.shiro.model.AuthenticationInfo
import com.jerry.request_shiro.shiro.model.AuthorizationInfo
import com.jerry.rt.core.http.pojo.Request


interface IShiroAuth {
    //认证,返回token
    fun onAuthentication(authToken: AuthToken): AuthenticationInfo?

    //授权
    fun onAuthorization(authorization: AuthenticationInfo):AuthorizationInfo

    //获取访问者的token
    fun getAccessToken(request: Request,shiroTokenName:String):String?{
        return request.getPackage().getHeader().getCookie(shiroTokenName)
    }
}
