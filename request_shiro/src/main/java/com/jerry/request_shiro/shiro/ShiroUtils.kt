package com.jerry.request_shiro.shiro

import com.jerry.request_shiro.shiro.config.ShiroConfig
import com.jerry.request_shiro.shiro.exception.ShiroAuthException
import com.jerry.request_shiro.shiro.impl.ShiroCache
import com.jerry.request_shiro.shiro.impl.ShiroCacheManager
import com.jerry.request_shiro.shiro.interfaces.IShiroAuth
import com.jerry.request_shiro.shiro.interfaces.IShiroCache
import com.jerry.request_shiro.shiro.interfaces.IShiroCacheManager
import com.jerry.request_shiro.shiro.interfaces.UserLoginToken
import com.jerry.request_shiro.shiro.model.*
import com.jerry.rt.core.http.pojo.Cookie
import com.jerry.rt.core.http.pojo.Request
import java.util.UUID

/**
 * 如果是以token形式来进行认证的话，目前用session保存是无效，客户端一旦断开，session立马就会失效
 * 重新写一个缓存接口，使其可以自定怎么增加缓存，删除缓存，并且可以设置缓存有效期，或者定时删除缓存
 */
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

    internal var cacheManager:IShiroCacheManager = ShiroCacheManager()


    fun login(userLoginToken: UserLoginToken):String{
        val token = iShiroAuth.getAccessToken(userLoginToken.getRequest(), shiroConfig.tokenName)
        if (token!=null){
            val shiroInfo = cacheManager.getCache(token)?.getValue(shiroConfig.tokenName, null) as? ShiroInfo
            if (shiroInfo!=null){
                return shiroInfo.authenticationInfo.token
            }
        }

        val onAuthentication = iShiroAuth.onAuthentication(AuthToken(userLoginToken.getUserName(),userLoginToken.getPassword()))
        val onAuthorization = iShiroAuth.onAuthorization(onAuthentication)

        cacheManager.addCache(IShiroCache().apply {
            setId(onAuthentication.token)
            setMaxAge(shiroConfig.validTime*1000L)
            putValue(shiroConfig.tokenName,ShiroInfo(onAuthorization,onAuthentication))
        })
        userLoginToken.getResponse().addCookie(Cookie(shiroConfig.tokenName, value = onAuthentication.token, maxAge = shiroConfig.validTime, path = "/"))
        return onAuthentication.token
    }

    fun logout(request: Request){
        val session = request.getPackage().getSession()
        session.removeAttribute(shiroConfig.tokenName)
    }

    fun getShiroInfo(request: Request) = request.getPackage().getSession().getAttribute(shiroConfig.tokenName) as? ShiroInfo ?: throw ShiroAuthException("no valid token")
}