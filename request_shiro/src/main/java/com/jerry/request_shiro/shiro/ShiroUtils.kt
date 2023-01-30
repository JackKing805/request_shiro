package com.jerry.request_shiro.shiro

import com.jerry.request_core.Core
import com.jerry.request_shiro.shiro.config.ShiroConfiguration
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.interfaces.UserLoginToken
import com.jerry.request_shiro.shiro.model.*
import com.jerry.request_shiro.shiro.register.ShiroConfigRegister
import com.jerry.rt.core.http.pojo.Cookie
import com.jerry.rt.core.http.pojo.Request
import java.time.Duration
import java.util.UUID

object ShiroUtils {
    internal const val SHIRO_TOKEN = "ShiroToken"

    internal var iShiroAuth: IShiroAuth = object :IShiroAuth{
        override fun onAuthentication(authToken: AuthToken): AuthenticationInfo {
            return AuthenticationInfo(authToken,authToken.getPassword(),UUID.randomUUID().toString())
        }

        override fun onAuthorization(authorization: AuthenticationInfo): AuthorizationInfo {
            return AuthorizationInfo()
        }
    }

    fun getConfigurationList() = listOf<Class<*>>(
        ShiroConfigRegister::class.java,
        ShiroConfiguration::class.java
    )

    fun login(userLoginToken: UserLoginToken):String{
        val session = userLoginToken.getRequest().getPackage().getSession()
        val shiroToken = session.getAttribute("shiro_token") as? ShiroToken

        if (shiroToken!=null){
            return shiroToken.authenticationInfo.token
        }

        val onAuthentication = iShiroAuth.onAuthentication(AuthToken(userLoginToken.getUserName(),userLoginToken.getPassword()))

        val onAuthorization = iShiroAuth.onAuthorization(onAuthentication)

        session.setAttribute("shiro_token",ShiroToken(onAuthorization,onAuthentication))

        userLoginToken.getResponse().addCookie(Cookie(SHIRO_TOKEN, value = onAuthentication.token, maxAge = (Core.getRtConfig().rtSessionConfig.sessionValidTime.toMillis()/1000).toInt()))
        return onAuthentication.token
    }

    fun logout(request: Request){
        val session = request.getPackage().getSession()
        session.removeAttribute("shiro_token")
    }
}