package com.jerry.request_shiro.shiro

import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.exception.ShiroException
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.interfaces.UserLoginToken
import com.jerry.request_shiro.shiro.model.*
import com.jerry.rt.core.http.pojo.Cookie
import com.jerry.rt.core.http.pojo.Request
import java.util.UUID

object ShiroUtils {
    internal var shiroConfig =  ShiroConfig("shiro_token",900)

    internal var iShiroAuth: IShiroAuth = object :IShiroAuth{
        override fun onAuthentication(authToken: AuthToken): AuthenticationInfo {
            return AuthenticationInfo(authToken,authToken.getPassword(),UUID.randomUUID().toString())
        }

        override fun onAuthorization(authorization: AuthenticationInfo): AuthorizationInfo {
            return AuthorizationInfo()
        }
    }

    fun login(userLoginToken: UserLoginToken):String{
        val session = userLoginToken.getRequest().getPackage().getSession()
        val shiroInfo = session.getAttribute(shiroConfig.tokenName) as? ShiroInfo

        if (shiroInfo!=null){
            return shiroInfo.authenticationInfo.token
        }

        val onAuthentication = iShiroAuth.onAuthentication(AuthToken(userLoginToken.getUserName(),userLoginToken.getPassword()))

        val onAuthorization = iShiroAuth.onAuthorization(onAuthentication)

        session.setAttribute(shiroConfig.tokenName,ShiroInfo(onAuthorization,onAuthentication))

        userLoginToken.getResponse().addCookie(Cookie(shiroConfig.tokenName, value = onAuthentication.token, maxAge = shiroConfig.validTime, path = "/"))
        return onAuthentication.token
    }

    fun logout(request: Request){
        val session = request.getPackage().getSession()
        session.removeAttribute(shiroConfig.tokenName)
    }

    fun getShiroInfo(request: Request) = request.getPackage().getSession().getAttribute(shiroConfig.tokenName) as? ShiroInfo ?: throw ShiroException("no valid token")
}